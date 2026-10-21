# CampusPulse — Phase 3 Implementation Report: SQLite Database Integration

**Project:** CampusPulse (Java MVC Architecture)  
**Phase:** Phase 3 — SQLite Database Integration  
**Owner:** Alan Saji (Authentication, Database Integration, Admin User Management, Integration/Testing)  
**Target Branch:** `feature/alan-auth`  
**Date:** October 22, 2026  

---

## 1. Executive Summary

In Phase 3, persistent relational storage was integrated into CampusPulse using SQLite and JDBC. The `UserRepository` model was upgraded from transient in-memory collections to SQLite JDBC persistence, while preserving all existing public method signatures and behaviors.

Account deactivations, user registrations, and authentication records now persist across application restarts. Database connection handling strictly adheres to safe resource management practices using try-with-resources. Complete test isolation ensures that running tests creates isolated temporary databases without mutating development or production data.

---

## 2. Dependency Specification and Setup

To maintain a lightweight, pure-Java project without requiring build automation tools (such as Maven or Gradle), the standalone official SQLite JDBC driver is utilized.

### 2.1 Artifact Coordinates
- **Group ID:** `org.xerial`
- **Artifact ID:** `sqlite-jdbc`
- **Version:** `3.40.0.0`
- **Packaging:** JAR
- **Location:** `lib/sqlite-jdbc-3.40.0.0.jar`
- **Official Maven Central URL:**  
  `https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.0.0/sqlite-jdbc-3.40.0.0.jar`

> **Note on Version Selection:** Version `3.40.0.0` packages native SQLite binaries for all major platforms (Linux x86/ARM, macOS, Windows) and requires no external logging runtime dependencies (such as SLF4J), ensuring turnkey execution via plain `javac` and `java -cp`.

### 2.2 Download Instructions (Kali Linux)
Teammates cloning the repository can reproducibly obtain the driver JAR into `lib/` using:
```bash
mkdir -p lib
curl -fSL https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.0.0/sqlite-jdbc-3.40.0.0.jar \
     -o lib/sqlite-jdbc-3.40.0.0.jar
```

---

## 3. SQLite Schema and Persistence Architecture

### 3.1 Table Definition & Schema Initialization
The `users` table and indexes are created idempotently during repository initialization using separate SQL statement executions:

```sql
CREATE TABLE IF NOT EXISTS users (
    user_id   TEXT PRIMARY KEY,
    name      TEXT NOT NULL,
    email     TEXT NOT NULL UNIQUE COLLATE NOCASE,
    phone     TEXT,
    password  TEXT NOT NULL,
    role      TEXT NOT NULL,
    is_active INTEGER NOT NULL DEFAULT 1
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
```

### 3.2 Key Design Choices
1. **Case-Insensitive Email Matching:**  
   The `email` column uses `COLLATE NOCASE`. This guarantees that case-insensitive email matching is enforced at both the unique constraint level and query execution level without altering case in user-facing data.
2. **Persistence of Active/Inactive State:**  
   Account status is stored directly in SQLite (`is_active INTEGER DEFAULT 1`). Deactivated users have `is_active = 0`. This replaces the previous in-memory `Set<String> deactivatedUserIds` while preserving the exact `User` model fields owned by teammate Ananya Raju.
3. **Explicit Parent Directory Validation:**  
   Before connecting to SQLite, the repository checks if the parent directory exists. If `mkdirs()` fails, an explicit `SQLException` is thrown with the absolute path, preventing silent initialization failures.
4. **Separate Statement Execution:**  
   `initSchema()` executes `CREATE TABLE` and `CREATE INDEX` as distinct JDBC statements within a try-with-resources block, ensuring standard JDBC driver compliance.
5. **Short-Lived Connections & Prepared Statements:**  
   To prevent connection leaks and ensure database concurrency safety, database connections, statements, and result sets are managed strictly via try-with-resources per operation. Parameterized queries (`PreparedStatement`) prevent SQL injection.
6. **Configurable Database Path:**  
   The database location is determined dynamically:
   - System property: `campuspulse.db.path`
   - Default fallback: `data/campuspulse.db`
   - Constructor overload: `new UserRepository(String dbPath)` for test isolation.

---

## 4. Files Added and Modified

| File | Status | Description & Ownership Justification |
|------|--------|---------------------------------------|
| `src/model/UserRepository.java` | Modified | Rewritten to persist data via SQLite JDBC while maintaining the exact existing public API (`addUser`, `findByEmail`, `findById`, `getAllUsers`, `deactivateUser`, `isUserActive`). Includes explicit parent dir checks and separated schema statement execution. Alan Saji ownership. |
| `src/DatabaseTest.java` | Added | Dedicated verification suite containing 14 test scenarios (53 assertions) covering schema, persistence, isolation, constraints, error handling, and controller integration. Alan Saji ownership. |
| `src/AuthTest.java` | Modified | Updated test runner to use an isolated temporary database with automatic cleanup, preventing interference with dev data. Alan Saji ownership. |
| `src/AdminTest.java` | Modified | Updated test runner to create isolated temporary database instances per test case with automatic cleanup, and safely restores `campuspulse.db.path` in a `try...finally` block. Alan Saji ownership. |
| `.gitignore` | Modified | Added patterns to ignore SQLite database files (`*.db`, `*.db-journal`, `*.db-wal`, `*.db-shm`) and the `data/` runtime directory, preserving `*.jar` ignore rule with a narrow exception for this report (`!docs/reports/phase-3-database-integration-report.md`). |
| `docs/reports/phase-3-database-integration-report.md` | Added | Detailed phase implementation report. |

> **Ownership Boundary Verification:**  
> Zero modifications were made to teammate-owned files:  
> - `src/model/User.java` (Ananya Raju) — untouched.  
> - `src/util/AppConstants.java` (Ananya Raju) — untouched.  
> - `src/view/MainView.java` (Ananya Raju) — untouched.  
> - `src/model/Resource.java` (Ardra K Gireesh) — untouched.  
> - `src/model/Request.java` / `Volunteer.java` (Ashish K Biju) — untouched.  
> - `MainController.java` Sections 2, 3, and 5 — untouched.

---

## 5. Compilation and Execution Guide

### 5.1 Project Compilation
Compile all Java source files using the SQLite JDBC classpath:
```bash
javac -cp "lib/sqlite-jdbc-3.40.0.0.jar:." -d out $(find src -name "*.java")
```

### 5.2 Running the Test Suites

#### 1. Authentication Tests (`AuthTest`)
```bash
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" AuthTest
```

#### 2. Admin User Management Tests (`AdminTest`)
```bash
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" AdminTest
```

#### 3. Database Integration Tests (`DatabaseTest`)
```bash
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" DatabaseTest
```

### 5.3 Configuring the Database Path
To run with a custom database location (e.g. testing or staging):
```bash
java -Dcampuspulse.db.path=/custom/path/my_database.db -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" Main
```

### 5.4 Inspecting Database with `sqlite3` CLI
To inspect tables and records using the Linux CLI tool:
```bash
sqlite3 data/campuspulse.db
```
Useful SQL inspection queries:
```sql
.tables
.schema users
SELECT user_id, name, email, role, is_active FROM users;
.exit
```

---

## 6. Verification and Test Results

All test suites were executed on Kali Linux.

### 6.1 Authentication Tests (`AuthTest`)
- **TC-1:** Correct credentials → login returns true (`PASS`)
- **TC-2:** Wrong password → login returns false (`PASS`)
- **TC-3:** Unknown email → login returns false (`PASS`)
- **TC-4:** Uppercase email → login returns true (case-insensitive) (`PASS`)
- **TC-5a–c:** Post-login user session verification (`PASS`)
- **TC-6:** Post-logout null session verification (`PASS`)
- **TC-7:** Failed login does not retain stale session (`PASS`)
- **Total:** **9 PASSED, 0 FAILED**

### 6.2 Admin User Management Tests (`AdminTest`)
- **TC-1a–b:** `getAllUsers()` on empty repository (`PASS`)
- **TC-2a–c:** `getAllUsers()` retrieval count and membership (`PASS`)
- **TC-3a–b:** Immutability of returned user collection (`PASS`)
- **TC-4a–c:** Deactivation of existing user (`PASS`)
- **TC-5:** Deactivation of unknown ID handled safely (`PASS`)
- **TC-6a–e:** Safe handling of null and blank identifiers (`PASS`)
- **TC-7a–c:** Deactivated users remain queryable (`PASS`)
- **TC-8a–c:** Deactivated users rejected on login (`PASS`)
- **TC-9a–b:** Active users login unaffected (`PASS`)
- **TC-10a–c:** Targeted deactivation leaves other users intact (`PASS`)
- **TC-11a–d:** Repeated idempotent deactivations (`PASS`)
- **TC-12a–e:** Admin model delegation and safe null handling (`PASS`)
- **TC-13a–b:** MainController Section 4 integration with clean environment restoration (`PASS`)
- **Total:** **38 PASSED, 0 FAILED**

### 6.3 Database Integration Tests (`DatabaseTest`)
- **TC-1:** Schema initialization and DB file creation (`PASS`)
- **TC-2a–g:** Insertion and multi-field retrieval (`PASS`)
- **TC-3a–c:** Persistence across distinct repository instances / app restart (`PASS`)
- **TC-4a–d:** Case-insensitive email lookups (upper, lower, mixed) (`PASS`)
- **TC-5a–h:** Safe handling of unknown, null, and empty queries (`PASS`)
- **TC-6a–c:** Primary key and case-insensitive unique email constraint enforcement (`PASS`)
- **TC-7a–b:** Collection retrieval and unmodifiable view protection (`PASS`)
- **TC-8a–e:** Deactivation persistence across repository instances (`PASS`)
- **TC-9a–c:** Rejected authentication for deactivated users (`PASS`)
- **TC-10a–c:** Successful authentication for active users (`PASS`)
- **TC-11a–d:** Password failure and unknown email rejection (`PASS`)
- **TC-12a–c:** Logout and failed-login session cleanup (`PASS`)
- **TC-13a–c:** Admin model integration (`PASS`)
- **TC-14a–d:** Database validation errors and uncreatable directory throw clear exceptions (`PASS`)
- **Total:** **53 PASSED, 0 FAILED**

---

## 7. Known Limitations & Future Work

1. **Plaintext Password Storage:** Passwords are currently persisted and verified in plaintext. Password hashing (e.g. PBKDF2 with salt) is slated for a future security hardening phase.
2. **Unrelated Features In-Memory:** Request matching and volunteer tracking managed by teammates currently remain in-memory pending their respective persistence phases.
3. **Connection Pooling:** For the current prototype and single-user CLI interface, direct connection creation is sufficient; connection pooling (e.g. HikariCP) can be introduced if multi-threaded concurrent web traffic is added later.

---

## 8. Git & Pull Request Readiness

- **Branch:** `feature/alan-auth`
- **Git Operations:** No Git commands (`commit`, `push`, `merge`, `checkout`, `rebase`) were executed. All changes are uncommitted in the local working tree, ready for developer review and commit.
