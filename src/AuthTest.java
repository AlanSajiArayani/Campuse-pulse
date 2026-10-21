import controller.AuthController;
import model.User;
import model.UserRepository;

/**
 * AuthTest.java
 * Standalone authentication tests for CampusPulse — Phase 1.
 * Tests AuthController directly using an isolated UserRepository instance.
 * No JUnit required: uses plain-Java assertions and System.exit(1) on failure.
 *
 * LIMITATION: passwords are compared in plaintext in this prototype.
 * Secure hashing (PBKDF2) is deferred to a later phase.
 *
 * Owner: Alan Saji
 * Do not seed test users from Main.java or application startup.
 */
public class AuthTest {

    // -------------------------------------------------------------------------
    // Test runner state
    // -------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== CampusPulse — Authentication Tests (Phase 1) ===\n");

        // -------------------------------------------------------------------------
        // Test setup: one repository + one controller shared across all tests.
        // A second user is added to support the "no stale session" test.
        // -------------------------------------------------------------------------
        UserRepository repository = new UserRepository();

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

        // -------------------------------------------------------------------------
        // TC-1  Correct email + correct password → login succeeds
        // -------------------------------------------------------------------------
        {
            boolean result = auth.login("student@example.com", "secret-pass");
            assertEqual("TC-1  Correct credentials → login returns true", true, result);
            auth.logout();
        }

        // -------------------------------------------------------------------------
        // TC-2  Correct email + WRONG password → login fails
        // -------------------------------------------------------------------------
        {
            boolean result = auth.login("student@example.com", "wrong-password");
            assertEqual("TC-2  Wrong password → login returns false", false, result);
        }

        // -------------------------------------------------------------------------
        // TC-3  Unknown email → login fails
        // -------------------------------------------------------------------------
        {
            boolean result = auth.login("nobody@example.com", "secret-pass");
            assertEqual("TC-3  Unknown email → login returns false", false, result);
        }

        // -------------------------------------------------------------------------
        // TC-4  Case-insensitive email match (repository uses equalsIgnoreCase)
        // -------------------------------------------------------------------------
        {
            boolean result = auth.login("STUDENT@EXAMPLE.COM", "secret-pass");
            assertEqual("TC-4  Uppercase email → login returns true (case-insensitive)", true, result);
            auth.logout();
        }

        // -------------------------------------------------------------------------
        // TC-5  After successful login, getCurrentUser() returns the correct user
        // -------------------------------------------------------------------------
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

        // -------------------------------------------------------------------------
        // TC-6  After logout, getCurrentUser() returns null
        // -------------------------------------------------------------------------
        {
            auth.login("student@example.com", "secret-pass");
            auth.logout();
            assertNull("TC-6  After logout, getCurrentUser() is null", auth.getCurrentUser());
        }

        // -------------------------------------------------------------------------
        // TC-7  After a FAILED login, no previously authenticated user remains active.
        //        Login as admin first → then attempt wrong credentials → user must be null.
        // -------------------------------------------------------------------------
        {
            auth.login("admin@example.com", "admin-pass");   // success
            auth.logout();                                    // clean state
            auth.login("student@example.com", "wrong");      // intentional failure
            assertNull("TC-7  Failed login does not preserve previous session",
                       auth.getCurrentUser());
        }

        // -------------------------------------------------------------------------
        // Summary
        // -------------------------------------------------------------------------
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