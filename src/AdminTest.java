import controller.AuthController;
import controller.MainController;
import model.Admin;
import model.User;
import model.UserRepository;
import view.MainView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminTest.java
 * Standalone verification tests for CampusPulse Phase 2 — Admin User Management.
 * Tests user retrieval, deactivation state in UserRepository, Admin model operations,
 * and authentication enforcement for deactivated users.
 *
 * Uses isolated SQLite test databases to guarantee repeatable execution without mutating dev data.
 * No external test framework required: uses plain-Java assertions and System.exit(1) on failure.
 * Owner: Alan Saji
 */
public class AdminTest {

    private static int passed = 0;
    private static int failed = 0;
    private static final List<String> testDbs = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== CampusPulse — Admin User Management Tests (Phase 2) ===\n");

        try {
            // -----------------------------------------------------------------
            // TC-1: getAllUsers() returns an empty collection for an empty repository
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                List<User> all = repo.getAllUsers();
                assertNotNull("TC-1a getAllUsers() on empty repo is non-null", all);
                assertEqual("TC-1b getAllUsers() on empty repo is empty", 0, all.size());
            }

            // -----------------------------------------------------------------
            // TC-2: getAllUsers() returns all registered users
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                User u1 = new User("U101", "Alice", "alice@example.com", "1111111111", "pass1", "STUDENT");
                User u2 = new User("U102", "Bob", "bob@example.com", "2222222222", "pass2", "STAFF");
                repo.addUser(u1);
                repo.addUser(u2);

                List<User> all = repo.getAllUsers();
                assertEqual("TC-2a getAllUsers() count matches registered count", 2, all.size());
                boolean containsU1 = all.stream().anyMatch(u -> "U101".equals(u.getUserId()));
                boolean containsU2 = all.stream().anyMatch(u -> "U102".equals(u.getUserId()));
                assertEqual("TC-2b Contains Alice (U101)", true, containsU1);
                assertEqual("TC-2c Contains Bob (U102)", true, containsU2);
            }

            // -----------------------------------------------------------------
            // TC-3: Modifying the returned collection cannot mutate the repository's internal list
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U101", "Alice", "alice@example.com", "111", "p", "STUDENT"));
                List<User> returned = repo.getAllUsers();

                boolean mutationBlocked = false;
                try {
                    returned.add(new User("U999", "Intruder", "intruder@example.com", "000", "p", "STUDENT"));
                } catch (UnsupportedOperationException e) {
                    mutationBlocked = true;
                }
                assertEqual("TC-3a Modifying returned list throws UnsupportedOperationException", true, mutationBlocked);
                assertEqual("TC-3b Repository internal size remains 1", 1, repo.getAllUsers().size());
            }

            // -----------------------------------------------------------------
            // TC-4: Deactivating an existing user succeeds
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U201", "Charlie", "charlie@example.com", "333", "pass", "STUDENT"));

                assertEqual("TC-4a User is active initially", true, repo.isUserActive("U201"));
                boolean deactSuccess = repo.deactivateUser("U201");
                assertEqual("TC-4b deactivateUser() returns true for existing user", true, deactSuccess);
                assertEqual("TC-4c isUserActive() returns false after deactivation", false, repo.isUserActive("U201"));
            }

            // -----------------------------------------------------------------
            // TC-5: Deactivating an unknown ID is handled safely
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U201", "Charlie", "charlie@example.com", "333", "pass", "STUDENT"));

                boolean unknownDeact = repo.deactivateUser("NONEXISTENT_ID");
                assertEqual("TC-5 Deactivating unknown ID safely returns false", false, unknownDeact);
            }

            // -----------------------------------------------------------------
            // TC-6: A null or blank ID is handled safely
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                assertEqual("TC-6a Deactivating null ID returns false", false, repo.deactivateUser(null));
                assertEqual("TC-6b Deactivating empty ID returns false", false, repo.deactivateUser(""));
                assertEqual("TC-6c Deactivating whitespace ID returns false", false, repo.deactivateUser("   "));
                assertEqual("TC-6d isUserActive(null) safely returns false", false, repo.isUserActive(null));
                assertEqual("TC-6e isUserActive(\"\") safely returns false", false, repo.isUserActive(""));
            }

            // -----------------------------------------------------------------
            // TC-7: A deactivated user remains present in the repository
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                User u = new User("U301", "David", "david@example.com", "444", "pass", "VOLUNTEER");
                repo.addUser(u);
                repo.deactivateUser("U301");

                User foundById = repo.findById("U301");
                assertNotNull("TC-7a Deactivated user is still found by findById()", foundById);
                User foundByEmail = repo.findByEmail("david@example.com");
                assertNotNull("TC-7b Deactivated user is still found by findByEmail()", foundByEmail);
                assertEqual("TC-7c Deactivated user remains in getAllUsers()", 1, repo.getAllUsers().size());
            }

            // -----------------------------------------------------------------
            // TC-8: A deactivated user cannot log in with correct credentials
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U401", "Eve", "eve@example.com", "555", "eve-secret", "STUDENT"));
                AuthController auth = new AuthController(repo);

                assertEqual("TC-8a Login succeeds before deactivation", true, auth.login("eve@example.com", "eve-secret"));
                auth.logout();

                repo.deactivateUser("U401");
                boolean loginAfterDeact = auth.login("eve@example.com", "eve-secret");
                assertEqual("TC-8b Deactivated user rejected on login", false, loginAfterDeact);
                assertNull("TC-8c No active session retained after rejected login", auth.getCurrentUser());
            }

            // -----------------------------------------------------------------
            // TC-9: An active user can still log in
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U501", "Frank", "frank@example.com", "666", "frank-secret", "STUDENT"));
                AuthController auth = new AuthController(repo);

                boolean loginResult = auth.login("frank@example.com", "frank-secret");
                assertEqual("TC-9a Active user can log in successfully", true, loginResult);
                assertNotNull("TC-9b Active user session established", auth.getCurrentUser());
                auth.logout();
            }

            // -----------------------------------------------------------------
            // TC-10: An unrelated user's authentication remains unaffected
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U601", "Grace", "grace@example.com", "777", "grace-pass", "STUDENT"));
                repo.addUser(new User("U602", "Heidi", "heidi@example.com", "888", "heidi-pass", "STUDENT"));
                AuthController auth = new AuthController(repo);

                // Deactivate Grace only
                repo.deactivateUser("U601");

                assertEqual("TC-10a Deactivated user (Grace) cannot log in", false, auth.login("grace@example.com", "grace-pass"));
                assertEqual("TC-10b Unrelated active user (Heidi) can log in", true, auth.login("heidi@example.com", "heidi-pass"));
                assertEqual("TC-10c Current user is Heidi", "U602", auth.getCurrentUser() != null ? auth.getCurrentUser().getUserId() : null);
                auth.logout();
            }

            // -----------------------------------------------------------------
            // TC-11: Repeated deactivation behaves consistently
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U701", "Ivan", "ivan@example.com", "999", "ivan-pass", "STUDENT"));

                boolean firstDeact = repo.deactivateUser("U701");
                assertEqual("TC-11a First deactivation returns true", true, firstDeact);
                assertEqual("TC-11b User is deactivated", false, repo.isUserActive("U701"));

                boolean secondDeact = repo.deactivateUser("U701");
                assertEqual("TC-11c Repeated deactivation returns true (consistent idempotent state)", true, secondDeact);
                assertEqual("TC-11d User remains deactivated", false, repo.isUserActive("U701"));
            }

            // -----------------------------------------------------------------
            // TC-12: Admin model viewAllUsers() and deactivateUser()
            // -----------------------------------------------------------------
            {
                UserRepository repo = createRepo();
                repo.addUser(new User("U801", "Judy", "judy@example.com", "123", "judy-pass", "STUDENT"));

                Admin admin = new Admin("A001", "SuperAdmin", "admin@campus.edu", "000", "adm-pass", "ADM-01", repo);
                assertEqual("TC-12a Admin adminId getter", "ADM-01", admin.getAdminId());
                assertEqual("TC-12b Admin getUserRepository() matches", repo, admin.getUserRepository());

                // Test viewAllUsers() does not throw
                boolean viewSucceeded = true;
                try {
                    admin.viewAllUsers();
                } catch (Exception e) {
                    viewSucceeded = false;
                }
                assertEqual("TC-12c admin.viewAllUsers() executes without error", true, viewSucceeded);

                // Test admin.deactivateUser()
                admin.deactivateUser("U801");
                assertEqual("TC-12d User deactivated via Admin model", false, repo.isUserActive("U801"));

                // Test admin with null repo handles safely
                Admin standaloneAdmin = new Admin();
                standaloneAdmin.viewAllUsers();
                standaloneAdmin.deactivateUser("U801");
                assertEqual("TC-12e Standalone admin handles null repo safely", true, true);
            }

            // -----------------------------------------------------------------
            // TC-13: MainController Section 4 integration
            // -----------------------------------------------------------------
            {
                String previousDbPath = System.getProperty("campuspulse.db.path");
                String tc13Db = "data/test_admin_tc13_" + System.currentTimeMillis() + ".db";
                testDbs.add(tc13Db);
                try {
                    System.setProperty("campuspulse.db.path", tc13Db);

                    MainView view = new MainView();
                    MainController controller = new MainController(view);

                    List<User> users = controller.getAllUsers();
                    assertNotNull("TC-13a MainController.getAllUsers() returns non-null list", users);

                    boolean deactSafe = true;
                    try {
                        controller.deactivateUser(null);
                        controller.deactivateUser("");
                        controller.deactivateUser("NONEXISTENT");
                    } catch (Exception e) {
                        deactSafe = false;
                    }
                    assertEqual("TC-13b MainController.deactivateUser() handles null/blank/unknown safely", true, deactSafe);
                } finally {
                    if (previousDbPath != null) {
                        System.setProperty("campuspulse.db.path", previousDbPath);
                    } else {
                        System.clearProperty("campuspulse.db.path");
                    }
                }
            }

        } finally {
            cleanupDbs();
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

    private static UserRepository createRepo() {
        String path = "data/test_admin_" + System.currentTimeMillis() + "_" + testDbs.size() + ".db";
        testDbs.add(path);
        return new UserRepository(path);
    }

    private static void cleanupDbs() {
        for (String path : testDbs) {
            new File(path).delete();
            new File(path + "-wal").delete();
            new File(path + "-shm").delete();
            new File(path + "-journal").delete();
        }
    }

    // -------------------------------------------------------------------------
    // Assertion helpers
    // -------------------------------------------------------------------------

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
