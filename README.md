# CampusPulse — Campus Resource & Volunteer Management System

A standalone desktop application built in pure **Java (Swing)** using **SQLite** for relational persistence. Designed under the **Model-View-Controller (MVC)** architectural pattern, CampusPulse provides an integrated platform for resource sharing, emergency assistance dispatching, and campus administration without requiring external application servers, Maven, or Gradle.

---

## Table of Contents

1. [System Architecture & Team Ownership](#system-architecture--team-ownership)
2. [How CampusPulse Works on Any Computer](#how-campuspulse-works-on-any-computer)
3. [Pre-Seeded Demo User Accounts](#pre-seeded-demo-user-accounts)
4. [Prerequisites & Dependencies](#prerequisites--dependencies)
5. [Step-by-Step Build & Run Instructions](#step-by-step-build--run-instructions)
   - [Linux & macOS](#linux--macos-bash--zsh)
   - [Windows (Command Prompt & PowerShell)](#windows-command-prompt--powershell)
6. [Running Test Suites](#running-test-suites)
7. [Core Application Workflows](#core-application-workflows)
8. [Project Directory Layout](#project-directory-layout)
9. [Troubleshooting & FAQ](#troubleshooting--faq)

---

## System Architecture & Team Ownership

CampusPulse was collaboratively designed and implemented by a four-member engineering team. Each member owns distinct, decoupled functional modules integrated via clean public APIs:

| Teammate | Core Responsibilities & Modular Ownership | Owned Files |
|---|---|---|
| **Alan Saji** | **Authentication, Database Integration, Admin Operations & Testing**<br>• SQLite JDBC persistence layer with connection safety<br>• Session management, case-insensitive email lookup<br>• Admin user activation/deactivation engine<br>• Automated test suites (`AuthTest`, `AdminTest`, `DatabaseTest`) | `UserRepository.java`<br>`AuthController.java`<br>`AdminTest.java`<br>`AuthTest.java`<br>`DatabaseTest.java`<br>`MainController.java` (Sec. 1 & 4) |
| **Ananya Raju** | **Application Architecture, Graphical UI & Domain Models**<br>• Swing desktop interface (`MainView`) with role-based tabbed cards<br>• Base `User` hierarchy (`Student`, `Staff`)<br>• Shared system constants and application routing | `MainView.java`<br>`User.java`<br>`Student.java`<br>`Staff.java`<br>`AppConstants.java`<br>`Main.java`<br>`MainController.java` (Sec. 5) |
| **Ardra K Gireesh** | **Resource Catalog, Inventory & Handover Security**<br>• Resource catalog management, search, and stock tracking<br>• Secure 4-digit OTP handover protocol preventing unauthorized transfers<br>• Request status tracking and state machine rules | `Resource.java`<br>`ResourceRequest.java`<br>`ResourceService.java`<br>`HandoverService.java`<br>`StatusTracker.java`<br>`Trackable.java`<br>`Verifiable.java`<br>`MainController.java` (Sec. 2) |
| **Ashish K Biju** | **Volunteer Management & Emergency Auto-Dispatch**<br>• Volunteer profile, availability statuses, and skillsets<br>• Assistance request lifecycle (`Request`)<br>• Algorithmic heuristic dispatch matching (+100 matching skill, -1 workload balancing, emergency fallback) | `Request.java`<br>`Volunteer.java`<br>`RequestService.java`<br>`StatusHistory.java`<br>`Demo.java`<br>`MainController.java` (Sec. 3) |

---

## How CampusPulse Works on Any Computer

When sharing code via GitHub, SQLite database files (`*.db`) and the `data/` runtime directory are intentionally excluded in `.gitignore`. This is standard industry best practice to prevent:
1. File locking conflicts between different operating systems.
2. Binary merge conflicts in Git history.
3. Overwriting teammate local testing states.

### Zero-Configuration Self-Bootstrapping

CampusPulse includes a built-in **Zero-Config Database Bootstrapper** inside `UserRepository` and `MainController`. When you clone this repository onto any fresh computer:

```
[Cloned Repository] (no data/ or .db present)
         │
         ▼
[Launch Main.java]
         │
         ├── 1. Directory Check ────► Automatically creates 'data/' folder on disk
         │
         ├── 2. SQLite Connection ──► Automatically generates 'data/campuspulse.db'
         │
         ├── 3. Schema DDL Execution ► Creates 'users' table and unique case-insensitive index
         │
         └── 4. Auto-Seeding Check ─► Detects empty DB (0 users) and automatically inserts
                                     pre-configured Student, Admin, Volunteer, and Staff accounts!
```

> **Zero Manual Setup:** Teammates and evaluators do **NOT** need to install SQLite, configure database servers, or run manual SQL import scripts. The system self-initializes completely on first launch.

---

## Pre-Seeded Demo User Accounts

Upon first launch, the application seeds the following credentials into SQLite for immediate evaluation. All accounts share the simple password **`psw`**:

| Role | Email | Password | User ID | Name | Initial Status |
|---|---|---|---|---|---|
| **Student** | `st@ex.com` | `psw` | `U001` | Test Student | Active |
| **Admin** | `ad@ex.com` | `psw` | `U002` | Test Admin | Active |
| **Volunteer** | `vol@ex.com` | `psw` | `U003` | Test Volunteer | Active (Skills: First Aid, Lab Assistant, Tutoring) |
| **Staff** | `sf@ex.com` | `psw` | `U004` | Test Staff | Active (Catalog Owner) |
| **Volunteer 2** | `vl@ex.com` | `psw` | `U005` | Volunteer 2 | Active |
| **Staff 2** | `stf@ex.com` | `psw` | `U006` | Staff 2 | **Deactivated** (For testing admin activation) |

> **Case-Insensitive Login:** Login queries use `COLLATE NOCASE` in SQLite. You can log in with `ST@EX.COM`, `St@Ex.Com`, or `st@ex.com`.

---

## Prerequisites & Dependencies

### 1. Java Development Kit (JDK)
- **Minimum Requirement:** JDK 11 or higher (OpenJDK or Oracle JDK 11, 17, or 21).
- Verify your installation:
  ```bash
  java -version
  javac -version
  ```

### 2. SQLite JDBC Driver
The project includes the standalone SQLite JDBC library in the `lib/` directory:
- **File:** `lib/sqlite-jdbc-3.40.0.0.jar`
- **Features:** Self-contains native SQLite binaries for Linux (x86_64/ARM), macOS (Intel/Apple Silicon), and Windows.
- If your Git clone did not include the JAR file (due to local environment settings), you can download it with a single command:
  - **Linux / macOS:**
    ```bash
    mkdir -p lib && curl -fSL https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.0.0/sqlite-jdbc-3.40.0.0.jar -o lib/sqlite-jdbc-3.40.0.0.jar
    ```
  - **Windows (PowerShell):**
    ```powershell
    if (!(Test-Path lib)) { New-Item -ItemType Directory -Path lib }; Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.0.0/sqlite-jdbc-3.40.0.0.jar" -OutFile "lib\sqlite-jdbc-3.40.0.0.jar"
    ```

---

## Step-by-Step Build & Run Instructions

Navigate to the project root directory (`Campuse-pulse/`) before running the commands below.

### Linux & macOS (Bash / Zsh)

#### 1. Compile All Source Files
```bash
javac -cp "lib/sqlite-jdbc-3.40.0.0.jar:." -d out $(find src -name "*.java")
```

#### 2. Run the Graphical Application
```bash
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" Main
```

---

### Windows (Command Prompt & PowerShell)

> **Important on Windows:** Windows uses a semicolon (`;`) as the classpath separator instead of a colon (`:`).

#### Option A: Windows PowerShell
```powershell
# 1. Compile
javac -cp "lib/sqlite-jdbc-3.40.0.0.jar;." -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })

# 2. Run
java -cp "lib/sqlite-jdbc-3.40.0.0.jar;out" Main
```

#### Option B: Windows Command Prompt (`cmd.exe`)
```cmd
:: 1. Generate list of Java sources
dir /s /b src\*.java > sources.txt

:: 2. Compile
javac -cp "lib/sqlite-jdbc-3.40.0.0.jar;." -d out @sources.txt

:: 3. Run
java -cp "lib/sqlite-jdbc-3.40.0.0.jar;out" Main
```

---

## Running Test Suites

CampusPulse comes with automated standalone test suites that require no JUnit or external test runners. All test suites execute with plain Java assertions (`assert`) and return exit code `0` on success.

| Test Suite | Scope & Coverage | Assertions |
|---|---|---|
| **`AuthTest`** | Credential checks, case-insensitivity, session lifecycle, failed login safety | 9 assertions |
| **`AdminTest`** | User listing, deactivation, reactivation, idempotency, null safety | 48 assertions |
| **`DatabaseTest`** | Schema generation, CRUD operations, constraint enforcement, SQL injection safety, isolation | 56 assertions |
| **`Demo`** | Full terminal simulation of resource lending, concurrency conflict, and OTP verification | Workflow simulation |

### Execution Commands (Linux / macOS)

```bash
# Run Authentication Tests (Phase 1)
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" AuthTest

# Run Admin & User Management Tests (Phase 2)
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" AdminTest

# Run SQLite Database Integration Tests (Phase 3)
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" DatabaseTest

# Run End-to-End Lending & Handover Simulation
java -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" Demo
```

*(On Windows, replace `:out` with `;out`)*

---

## Core Application Workflows

### 1. Student Portal (`st@ex.com` / `psw`)
- **Resource Catalog:** Search by keyword and category (Electronics, Books, Sports, Lab Equipment). Select an available item and click **Request to Borrow**.
- **My Borrowed Items & Handover:** View submitted borrow requests (`PENDING`, `ACCEPTED`, `COMPLETED`).
- **OTP Handover Verification:** Once Staff approves a request, enter the 4-digit security code received from staff to confirm item transfer.
- **Assistance & Emergency Requests:** Submit service requests or emergency alerts with required skills (e.g. *First Aid*, *Lab Assistant*).

### 2. Volunteer Portal (`vol@ex.com` / `psw`)
- **Availability Toggle:** Switch status between `AVAILABLE`, `BUSY`, and `UNAVAILABLE`.
- **Assigned Tasks:** View emergency and assistance requests auto-assigned to you based on your skill profile.
- **Task Resolution:** Mark tasks as `RESOLVED` when finished, which automatically frees your availability for subsequent emergency requests.
- **Skill Profile:** Add and update specialized competencies.

### 3. Staff Portal (`sf@ex.com` / `psw`)
- **Inventory Management:** Add new resources with category, quantity, and description.
- **Borrow Approvals:** Review student requests, accept valid loans, or decline requests when stock is depleted.
- **Handover Code Generation:** Generate a one-time 4-digit numeric handover code to share with the borrowing student during physical equipment transfer.

### 4. Admin Portal (`ad@ex.com` / `psw`)
- **User Management:** View all registered accounts stored in `data/campuspulse.db`.
- **Activation / Deactivation:** Click **Deactivate Selected User** or **Activate Selected User** to instantly update permissions in SQLite. Deactivated users are blocked from logging in immediately.
- **System Statistics:** View categorized counts of users, active volunteers, resources, and pending emergencies.

---

## Project Directory Layout

```
Campuse-pulse/
├── src/
│   ├── controller/
│   │   ├── AuthController.java          ← Session verification & login/logout logic
│   │   └── MainController.java          ← Central coordinator connecting UI, services & SQLite
│   ├── interfaces/
│   │   ├── Trackable.java               ← Interface for state tracking
│   │   └── Verifiable.java              ← Interface for OTP verification
│   ├── model/
│   │   ├── Admin.java                   ← Admin domain model
│   │   ├── Request.java                 ← Assistance/emergency request entity
│   │   ├── Resource.java                ← Inventory item model with thread-safe stock checks
│   │   ├── ResourceRequest.java         ← Lending slip entity
│   │   ├── Staff.java                   ← Staff user model
│   │   ├── StatusHistory.java           ← Audit log entry
│   │   ├── Student.java                 ← Student user model
│   │   ├── User.java                    ← Base user entity
│   │   ├── UserRepository.java          ← SQLite JDBC persistence layer
│   │   └── Volunteer.java               ← Volunteer entity with skill matching
│   ├── service/
│   │   ├── HandoverService.java         ← SHA-256 salted OTP code generator & verifier
│   │   ├── RequestService.java          ← Resource request state machine
│   │   ├── ResourceService.java         ← Catalog querying and inventory operations
│   │   └── StatusTracker.java           ← State transition validator and history log
│   ├── util/
│   │   └── AppConstants.java            ← Application-wide constants & roles
│   ├── view/
│   │   └── MainView.java                ← Swing graphical user interface
│   ├── AdminTest.java                   ← Phase 2 Admin test suite (48 assertions)
│   ├── AuthTest.java                    ← Phase 1 Authentication test suite (9 assertions)
│   ├── DatabaseTest.java                ← Phase 3 SQLite database test suite (56 assertions)
│   ├── Demo.java                        ← Terminal lifecycle simulation
│   └── Main.java                        ← Application launch entry point
├── docs/
│   ├── CampusPulse_Complete_Code_and_Database_Study_Guide.pdf ← Comprehensive study guide
│   ├── PROJECT_OVERVIEW.md              ← High-level functional specification
│   ├── PROJECT_STRUCTURE.md             ← File-by-file component breakdown
│   ├── TEAM_RESPONSIBILITIES.md         ← Individual contribution mapping
│   ├── SHARED_VARIABLES.md             ← Constants and cross-module contracts
│   ├── GIT_WORKFLOW.md                  ← Team branch & merge guidelines
│   └── reports/                         ← Implementation audit reports
├── lib/
│   └── sqlite-jdbc-3.40.0.0.jar         ← Standalone SQLite JDBC library
├── .gitignore                           ← Git exclusion rules (ignores *.db and data/)
└── README.md                            ← Project documentation and run guide
```

---

## Troubleshooting & FAQ

#### Q1: `ClassNotFoundException: org.sqlite.JDBC`
- **Cause:** The SQLite JDBC JAR is missing from the classpath.
- **Fix:** Ensure `lib/sqlite-jdbc-3.40.0.0.jar` exists and that `-cp` includes it:
  - On Linux/macOS: `-cp "lib/sqlite-jdbc-3.40.0.0.jar:out"`
  - On Windows: `-cp "lib/sqlite-jdbc-3.40.0.0.jar;out"`

#### Q2: How do I completely reset the database to a fresh state?
- Delete the `data/campuspulse.db` file:
  ```bash
  rm -f data/campuspulse.db*
  ```
  *(On Windows: `del data\campuspulse.db*`)*
- Re-launch `Main`. The application will automatically re-create the database and re-seed the default demo accounts.

#### Q3: "Database is locked" (`SQLITE_BUSY`)
- **Cause:** Another process or database viewer (e.g. SQLite browser) holds an open exclusive lock on `data/campuspulse.db`.
- **Fix:** Close external SQLite viewing tools before running the application. CampusPulse uses connection try-with-resources to ensure connections and locks are closed immediately after each query.

#### Q4: How do I test with custom database locations?
- You can override the default database path using the standard Java system property:
  ```bash
  java -Dcampuspulse.db.path=data/custom_test.db -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" Main
  ```

### 2. Volunteer Portal (`vol@ex.com` / `psw`)
- **Availability Toggle:** Switch status between `AVAILABLE`, `BUSY`, and `UNAVAILABLE`.
- **Assigned Tasks:** View emergency and assistance requests auto-assigned to you based on your skill profile.
- **Task Resolution:** Mark tasks as `RESOLVED` when finished, which automatically frees your availability for subsequent emergency requests.
- **Skill Profile:** Add and update specialized competencies.

### 3. Staff Portal (`sf@ex.com` / `psw`)
- **Inventory Management:** Add new resources with category, quantity, and description.
- **Borrow Approvals:** Review student requests, accept valid loans, or decline requests when stock is depleted.
- **Handover Code Generation:** Generate a one-time 4-digit numeric handover code to share with the borrowing student during physical equipment transfer.

### 4. Admin Portal (`ad@ex.com` / `psw`)
- **User Management:** View all registered accounts stored in `data/campuspulse.db`.
- **Activation / Deactivation:** Click **Deactivate Selected User** or **Activate Selected User** to instantly update permissions in SQLite. Deactivated users are blocked from logging in immediately.
- **System Statistics:** View categorized counts of users, active volunteers, resources, and pending emergencies.

---

## Project Directory Layout

```
Campuse-pulse/
├── src/
│   ├── controller/
│   │   ├── AuthController.java          ← Session verification & login/logout logic
│   │   └── MainController.java          ← Central coordinator connecting UI, services & SQLite
│   ├── interfaces/
│   │   ├── Trackable.java               ← Interface for state tracking
│   │   └── Verifiable.java              ← Interface for OTP verification
│   ├── model/
│   │   ├── Admin.java                   ← Admin domain model
│   │   ├── Request.java                 ← Assistance/emergency request entity
│   │   ├── Resource.java                ← Inventory item model with thread-safe stock checks
│   │   ├── ResourceRequest.java         ← Lending slip entity
│   │   ├── Staff.java                   ← Staff user model
│   │   ├── StatusHistory.java           ← Audit log entry
│   │   ├── Student.java                 ← Student user model
│   │   ├── User.java                    ← Base user entity
│   │   ├── UserRepository.java          ← SQLite JDBC persistence layer
│   │   └── Volunteer.java               ← Volunteer entity with skill matching
│   ├── service/
│   │   ├── HandoverService.java         ← SHA-256 salted OTP code generator & verifier
│   │   ├── RequestService.java          ← Resource request state machine
│   │   ├── ResourceService.java         ← Catalog querying and inventory operations
│   │   └── StatusTracker.java           ← State transition validator and history log
│   ├── util/
│   │   └── AppConstants.java            ← Application-wide constants & roles
│   ├── view/
│   │   └── MainView.java                ← Swing graphical user interface
│   ├── AdminTest.java                   ← Phase 2 Admin test suite (48 assertions)
│   ├── AuthTest.java                    ← Phase 1 Authentication test suite (9 assertions)
│   ├── DatabaseTest.java                ← Phase 3 SQLite database test suite (56 assertions)
│   ├── Demo.java                        ← Terminal lifecycle simulation
│   └── Main.java                        ← Application launch entry point
├── docs/
│   ├── CampusPulse_Complete_Code_and_Database_Study_Guide.pdf ← Comprehensive study guide
│   ├── PROJECT_OVERVIEW.md              ← High-level functional specification
│   ├── PROJECT_STRUCTURE.md             ← File-by-file component breakdown
│   ├── TEAM_RESPONSIBILITIES.md         ← Individual contribution mapping
│   ├── SHARED_VARIABLES.md             ← Constants and cross-module contracts
│   ├── GIT_WORKFLOW.md                  ← Team branch & merge guidelines
│   └── reports/                         ← Implementation audit reports
├── lib/
│   └── sqlite-jdbc-3.40.0.0.jar         ← Standalone SQLite JDBC library
├── .gitignore                           ← Git exclusion rules (ignores *.db and data/)
└── README.md                            ← Project documentation and run guide
```

---

## Troubleshooting & FAQ

#### Q1: `ClassNotFoundException: org.sqlite.JDBC`
- **Cause:** The SQLite JDBC JAR is missing from the classpath.
- **Fix:** Ensure `lib/sqlite-jdbc-3.40.0.0.jar` exists and that `-cp` includes it:
  - On Linux/macOS: `-cp "lib/sqlite-jdbc-3.40.0.0.jar:out"`
  - On Windows: `-cp "lib/sqlite-jdbc-3.40.0.0.jar;out"`

#### Q2: How do I completely reset the database to a fresh state?
- Delete the `data/campuspulse.db` file:
  ```bash
  rm -f data/campuspulse.db*
  ```
  *(On Windows: `del data\campuspulse.db*`)*
- Re-launch `Main`. The application will automatically re-create the database and re-seed the default demo accounts.

#### Q3: "Database is locked" (`SQLITE_BUSY`)
- **Cause:** Another process or database viewer (e.g. SQLite browser) holds an open exclusive lock on `data/campuspulse.db`.
- **Fix:** Close external SQLite viewing tools before running the application. CampusPulse uses connection try-with-resources to ensure connections and locks are closed immediately after each query.

#### Q4: How do I test with custom database locations?
- You can override the default database path using the standard Java system property:
  ```bash
  java -Dcampuspulse.db.path=data/custom_test.db -cp "lib/sqlite-jdbc-3.40.0.0.jar:out" Main
  ```

