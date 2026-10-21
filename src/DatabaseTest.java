import controller.AuthController;
import model.Admin;
import model.User;
import model.UserRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseTest.java
 * Standalone verification tests for CampusPulse Phase 3 — SQLite Database Integration.
 *
 * Verifies schema creation, persistence across repository instances, case-insensitive
 * email queries, constraint handling, deactivation persistence, and AuthController/Admin integration.
 *
 * No external test framework required: uses plain-Java assertions and System.exit(1) on failure.
 * Owner: Alan Saji
 */
public class DatabaseTest {

    private static int passed = 0;
    private static int failed = 0;
    private static final List<String> tempDbs = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== CampusPulse — SQLite Database Integration Tests (Phase 3) ===\n");

        try {
            // -----------------------------------------------------------------
            // TC-1: Schema initializes successfully
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc1");
                UserRepository repo = new UserRepository(dbPath);
                File dbFile = new File(dbPath);
                assertEqual("TC-1 Schema initialized and database file created", true, dbFile.exists());
            }

            // -----------------------------------------------------------------
            // TC-2: A user can be inserted and retrieved
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc2");
                UserRepository repo = new UserRepository(dbPath);
                User user = new User("U001", "Alice Student", "alice@test.edu", "1234567890", "pass123", "STUDENT");
                repo.addUser(user);

                User byId = repo.findById("U001");
                assertNotNull("TC-2a User found by ID", byId);
                assertEqual("TC-2b Name matches", "Alice Student", byId != null ? byId.getName() : null);
                assertEqual("TC-2c Email matches", "alice@test.edu", byId != null ? byId.getEmail() : null);
                assertEqual("TC-2d Phone matches", "1234567890", byId != null ? byId.getPhone() : null);
                assertEqual("TC-2e Role matches", "STUDENT", byId != null ? byId.getRole() : null);

                User byEmail = repo.findByEmail("alice@test.edu");
                assertNotNull("TC-2f User found by email", byEmail);
                assertEqual("TC-2g ID from email query matches", "U001", byEmail != null ? byEmail.getUserId() : null);
            }

            // -----------------------------------------------------------------
            // TC-3: User data persists across newly created repository instances
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc3");
                UserRepository repo1 = new UserRepository(dbPath);
                repo1.addUser(new User("U002", "Bob Smith", "bob@test.edu", "5551234567", "secret", "STAFF"));

                // Simulate app restart with a fresh UserRepository instance
                UserRepository repo2 = new UserRepository(dbPath);
                User retrieved = repo2.findById("U002");
                assertNotNull("TC-3a Retrieved user from second repository instance", retrieved);
                assertEqual("TC-3b Persistent user name matches", "Bob Smith", retrieved != null ? retrieved.getName() : null);
                assertEqual("TC-3c Persistent user email matches", "bob@test.edu", retrieved != null ? retrieved.getEmail() : null);
            }

            // -----------------------------------------------------------------
            // TC-4: Email lookup is case-insensitive
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc4");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U003", "Carol Danvers", "Carol.Danvers@Campus.Edu", "1112223333", "pass", "STUDENT"));

                User upperMatch = repo.findByEmail("CAROL.DANVERS@CAMPUS.EDU");
                assertNotNull("TC-4a Found by all-uppercase email", upperMatch);
                assertEqual("TC-4b ID matches for uppercase lookup", "U003", upperMatch != null ? upperMatch.getUserId() : null);

                User lowerMatch = repo.findByEmail("carol.danvers@campus.edu");
                assertNotNull("TC-4c Found by all-lowercase email", lowerMatch);

                User mixedMatch = repo.findByEmail("cArOl.DaNvErS@cAmPuS.eDu");
                assertNotNull("TC-4d Found by mixed-case email", mixedMatch);
            }

            // -----------------------------------------------------------------
            // TC-5: Unknown users return null or false
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc5");
                UserRepository repo = new UserRepository(dbPath);

                assertNull("TC-5a findById on nonexistent ID returns null", repo.findById("UNKNOWN"));
                assertNull("TC-5b findById with null returns null", repo.findById(null));
                assertNull("TC-5c findById with empty string returns null", repo.findById(""));
                assertNull("TC-5d findByEmail on nonexistent email returns null", repo.findByEmail("nobody@nowhere.com"));
                assertNull("TC-5e findByEmail with null returns null", repo.findByEmail(null));
                assertEqual("TC-5f isUserActive on nonexistent user returns false", false, repo.isUserActive("UNKNOWN"));
                assertEqual("TC-5g isUserActive on null returns false", false, repo.isUserActive(null));
                assertEqual("TC-5h deactivateUser on nonexistent user returns false", false, repo.deactivateUser("UNKNOWN"));
            }

            // -----------------------------------------------------------------
            // TC-6: Duplicate email or duplicate identifier handling is safe and clearly defined
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc6");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U004", "Original User", "unique@test.edu", "111", "pass", "STUDENT"));

                boolean duplicateIdCaught = false;
                try {
                    repo.addUser(new User("U004", "Duplicate ID", "other@test.edu", "222", "pass", "STUDENT"));
                } catch (RuntimeException e) {
                    duplicateIdCaught = true;
                }
                assertEqual("TC-6a Duplicate primary key (userId) throws RuntimeException", true, duplicateIdCaught);

                boolean duplicateEmailCaught = false;
                try {
                    repo.addUser(new User("U005", "Duplicate Email", "UNIQUE@TEST.EDU", "333", "pass", "STUDENT"));
                } catch (RuntimeException e) {
                    duplicateEmailCaught = true;
                }
                assertEqual("TC-6b Duplicate email (case-insensitive UNIQUE) throws RuntimeException", true, duplicateEmailCaught);

                assertEqual("TC-6c Repository row count remains 1 after failed duplicates", 1, repo.getAllUsers().size());
            }

            // -----------------------------------------------------------------
            // TC-7: getAllUsers() returns expected users and cannot mutate repository storage
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc7");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U007A", "User 1", "u1@test.edu", "1", "p", "STUDENT"));
                repo.addUser(new User("U007B", "User 2", "u2@test.edu", "2", "p", "STAFF"));

                List<User> list = repo.getAllUsers();
                assertEqual("TC-7a getAllUsers count is 2", 2, list.size());

                boolean mutationBlocked = false;
                try {
                    list.add(new User("U007C", "User 3", "u3@test.edu", "3", "p", "STUDENT"));
                } catch (UnsupportedOperationException e) {
                    mutationBlocked = true;
                }
                assertEqual("TC-7b Returned list is unmodifiable", true, mutationBlocked);
            }

            // -----------------------------------------------------------------
            // TC-8: User deactivation persists across repository instances
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc8");
                UserRepository repo1 = new UserRepository(dbPath);
                repo1.addUser(new User("U008", "David Miller", "david@test.edu", "444", "pass", "VOLUNTEER"));
                assertEqual("TC-8a User is initially active", true, repo1.isUserActive("U008"));

                boolean deact = repo1.deactivateUser("U008");
                assertEqual("TC-8b deactivateUser() returns true", true, deact);
                assertEqual("TC-8c User is inactive in instance 1", false, repo1.isUserActive("U008"));

                // Second repository instance on same database
                UserRepository repo2 = new UserRepository(dbPath);
                assertEqual("TC-8d Deactivation persisted in second instance", false, repo2.isUserActive("U008"));
                assertNotNull("TC-8e Deactivated user still present in repository", repo2.findById("U008"));

                // Reactivation persists across repository instances
                boolean act = repo2.activateUser("U008");
                assertEqual("TC-8f activateUser() returns true", true, act);
                assertEqual("TC-8g User is active in instance 2", true, repo2.isUserActive("U008"));

                UserRepository repo3 = new UserRepository(dbPath);
                assertEqual("TC-8h Reactivation persisted in third instance", true, repo3.isUserActive("U008"));
            }

            // -----------------------------------------------------------------
            // TC-9: Deactivated users cannot log in
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc9");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U009", "Eve White", "eve@test.edu", "555", "correct-pass", "STUDENT"));
                AuthController auth = new AuthController(repo);

                assertEqual("TC-9a Active user logs in", true, auth.login("eve@test.edu", "correct-pass"));
                auth.logout();

                repo.deactivateUser("U009");
                assertEqual("TC-9b Deactivated user rejected on login", false, auth.login("eve@test.edu", "correct-pass"));
                assertNull("TC-9c No session retained for deactivated user", auth.getCurrentUser());
            }

            // -----------------------------------------------------------------
            // TC-10: Active users can still log in with correct credentials
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc10");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U010", "Frank Black", "frank@test.edu", "666", "frank-secret", "STUDENT"));
                AuthController auth = new AuthController(repo);

                boolean success = auth.login("frank@test.edu", "frank-secret");
                assertEqual("TC-10a Active user login succeeds", true, success);
                assertNotNull("TC-10b Session established", auth.getCurrentUser());
                assertEqual("TC-10c Current user email correct", "frank@test.edu", auth.getCurrentUser().getEmail());
                auth.logout();
            }

            // -----------------------------------------------------------------
            // TC-11: Incorrect passwords and unknown emails are rejected
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc11");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U011", "Grace Hopper", "grace@test.edu", "777", "correct-pass", "ADMIN"));
                AuthController auth = new AuthController(repo);

                assertEqual("TC-11a Wrong password rejected", false, auth.login("grace@test.edu", "wrong-pass"));
                assertNull("TC-11b No session on wrong password", auth.getCurrentUser());

                assertEqual("TC-11c Unknown email rejected", false, auth.login("unknown@test.edu", "correct-pass"));
                assertNull("TC-11d No session on unknown email", auth.getCurrentUser());
            }

            // -----------------------------------------------------------------
            // TC-12: Logout and failed-login session clearing still work
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc12");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U012", "Hank Green", "hank@test.edu", "888", "hank-pass", "STUDENT"));
                AuthController auth = new AuthController(repo);

                auth.login("hank@test.edu", "hank-pass");
                assertNotNull("TC-12a Session active before logout", auth.getCurrentUser());
                auth.logout();
                assertNull("TC-12b Session cleared after logout", auth.getCurrentUser());

                // Failed login does not restore old user
                auth.login("hank@test.edu", "hank-pass");
                auth.login("hank@test.edu", "bad-pass");
                assertNull("TC-12c Session cleared after failed login attempt", auth.getCurrentUser());
            }

            // -----------------------------------------------------------------
            // TC-13: Admin operations integration with persisted database
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc13");
                UserRepository repo = new UserRepository(dbPath);
                repo.addUser(new User("U013A", "User A", "a@test.edu", "111", "pass", "STUDENT"));
                repo.addUser(new User("U013B", "User B", "b@test.edu", "222", "pass", "STUDENT"));

                Admin admin = new Admin("ADM1", "Admin Boss", "admin@test.edu", "999", "adminpass", "A-01", repo);
                assertEqual("TC-13a Admin gets repository", repo, admin.getUserRepository());

                admin.deactivateUser("U013A");
                assertEqual("TC-13b User A deactivated via Admin model", false, repo.isUserActive("U013A"));
                assertEqual("TC-13c User B remains active", true, repo.isUserActive("U013B"));
            }

            // -----------------------------------------------------------------
            // TC-14: Database errors do not produce false success results
            // -----------------------------------------------------------------
            {
                String dbPath = createTempDbPath("tc14");
                UserRepository repo = new UserRepository(dbPath);

                boolean nullAddCaught = false;
                try {
                    repo.addUser(null);
                } catch (IllegalArgumentException e) {
                    nullAddCaught = true;
                }
                assertEqual("TC-14a Adding null user throws IllegalArgumentException", true, nullAddCaught);

                boolean emptyIdCaught = false;
                try {
                    repo.addUser(new User("", "Name", "empty@test.edu", "123", "p", "STUDENT"));
                } catch (IllegalArgumentException e) {
                    emptyIdCaught = true;
                }
                assertEqual("TC-14b Adding user with empty ID throws IllegalArgumentException", true, emptyIdCaught);

                boolean nullEmailCaught = false;
                try {
                    repo.addUser(new User("U014", "Name", null, "123", "p", "STUDENT"));
                } catch (IllegalArgumentException e) {
                    nullEmailCaught = true;
                }
                assertEqual("TC-14c Adding user with null email throws IllegalArgumentException", true, nullEmailCaught);

                boolean invalidDirCaught = false;
                try {
                    File blocker = new File("data/blocker_file.tmp");
                    blocker.createNewFile();
                    tempDbs.add(blocker.getAbsolutePath());
                    new UserRepository("data/blocker_file.tmp/sub/test.db");
                } catch (RuntimeException | java.io.IOException e) {
                    invalidDirCaught = true;
                }
                assertEqual("TC-14d Explicit failure on uncreatable parent directory", true, invalidDirCaught);
            }

        } finally {
            cleanupTempDatabases();
        }

        // ---------------------------------------------------------------------
        // Summary
        // ---------------------------------------------------------------------
        System.out.println("\n--- Results ---");
        System.out.println("PASSED: " + passed);
        System.out.println("FAILED: " + failed);

        if (failed > 0) {
            System.err.println("\nSome tests FAILED. See above for details.");
            System.exit(1);
        } else {
            System.out.println("\nAll tests PASSED.");
        }
    }

    private static String createTempDbPath(String tag) {
        String path = "data/test_db_" + tag + "_" + System.currentTimeMillis() + "_" + tempDbs.size() + ".db";
        tempDbs.add(path);
        return path;
    }

    private static void cleanupTempDatabases() {
        for (String path : tempDbs) {
            deleteDbAndSidecars(path);
        }
    }

    private static void deleteDbAndSidecars(String path) {
        new File(path).delete();
        new File(path + "-wal").delete();
        new File(path + "-shm").delete();
        new File(path + "-journal").delete();
    }

    private static void assertEqual(String label, Object expected, Object actual) {
        boolean ok = (expected == null && actual == null)
                || (expected != null && expected.equals(actual));
        if (ok) {
            System.out.println("  PASS  " + label);
            passed++;
        } else {
            System.out.println("  FAIL  " + label
                    + " | expected=[" + expected + "] got=[" + actual + "]");
            failed++;
        }
    }

    private static void assertNotNull(String label, Object value) {
        if (value != null) {
            System.out.println("  PASS  " + label);
            passed++;
        } else {
            System.out.println("  FAIL  " + label + " | expected non-null but got null");
            failed++;
        }
    }

    private static void assertNull(String label, Object value) {
        if (value == null) {
            System.out.println("  PASS  " + label);
            passed++;
        } else {
            System.out.println("  FAIL  " + label + " | expected null but got [" + value + "]");
            failed++;
        }
    }
}

