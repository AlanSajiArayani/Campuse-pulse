import controller.AuthController;
import model.User;
import model.UserRepository;

import java.io.File;

public class AuthTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== CampusPulse — Authentication Tests (Phase 1) ===\n");

        String testDb = "data/test_auth_" + System.currentTimeMillis() + ".db";

        try {
            UserRepository repository = new UserRepository(testDb);

            User student = new User(
                "U001",
                "Test Student",
                "student@example.com",
                "9876543210",
                "secret-pass",
                "STUDENT"
            );
            User admin = new User(
                "U002",
                "Test Admin",
                "admin@example.com",
                "9000000001",
                "admin-pass",
                "ADMIN"
            );
            repository.addUser(student);
            repository.addUser(admin);

            AuthController auth = new AuthController(repository);

            {
                boolean result = auth.login("student@example.com", "secret-pass");
                assertEqual("TC-1  Correct credentials → login returns true", true, result);
                auth.logout();
            }

            {
                boolean result = auth.login("student@example.com", "wrong-password");
                assertEqual("TC-2  Wrong password → login returns false", false, result);
            }

            {
                boolean result = auth.login("nobody@example.com", "secret-pass");
                assertEqual("TC-3  Unknown email → login returns false", false, result);
            }

            {
                boolean result = auth.login("STUDENT@EXAMPLE.COM", "secret-pass");
                assertEqual("TC-4  Uppercase email → login returns true (case-insensitive)", true, result);
                auth.logout();
            }

            {
                auth.login("student@example.com", "secret-pass");
                User current = auth.getCurrentUser();
                assertNotNull("TC-5a After login, getCurrentUser() is not null", current);
                assertEqual("TC-5b getCurrentUser() email matches", "student@example.com",
                            current != null ? current.getEmail() : null);
                assertEqual("TC-5c getCurrentUser() userId matches", "U001",
                            current != null ? current.getUserId() : null);
                auth.logout();
            }

            {
                auth.login("student@example.com", "secret-pass");
                auth.logout();
                assertNull("TC-6  After logout, getCurrentUser() is null", auth.getCurrentUser());
            }

            {
                auth.login("admin@example.com", "admin-pass");
                auth.logout();
                auth.login("student@example.com", "wrong");
                assertNull("TC-7  Failed login does not preserve previous session",
                           auth.getCurrentUser());
            }

        } finally {
            cleanupDb(testDb);
        }

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

    private static void cleanupDb(String path) {
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
