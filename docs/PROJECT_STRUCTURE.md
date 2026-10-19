# CampusPulse — Project Structure

## Folder Map

```
Campuse-pulse/            ← repository root
├── src/                  ← all Java source code lives here
│   ├── model/            ← data classes (what the app knows about)
│   ├── view/             ← user interface classes (what the user sees)
│   ├── controller/       ← logic classes (what the app does)
│   ├── util/             ← helper constants and utilities
│   └── Main.java         ← application entry point
├── docs/                 ← all documentation files
├── .gitignore            ← tells git which files to ignore
└── README.md             ← quick-start guide for new team members
```

---

## Package Explanations (Plain English)

### `model/` — Data Classes

The **model** package holds simple Java classes that represent real-world objects.
They store data (fields) and provide getters/setters. They contain **no UI code**
and **no application logic** — just data.

Think of them as forms with blank fields waiting to be filled.

| File | What it represents |
|---|---|
| `User.java` | Any person who uses the system (base class) |
| `Student.java` | A student — extends `User`, adds `studentId`, `department`, `year` |
| `Volunteer.java` | A volunteer — extends `User`, adds availability and assigned requests |
| `Staff.java` | A staff member — extends `User`, adds `staffId` and `department` |
| `Admin.java` | An administrator — extends `User`, adds `adminId` |
| `Resource.java` | A physical/digital item in the campus catalog |
| `Request.java` | A borrow, assistance, or emergency request submitted by a user |

**Rule:** If a field already exists in `User`, do NOT add it again in a subclass.

---

### `view/` — User Interface

The **view** package handles everything the user sees on screen. It will use Java Swing
components (buttons, panels, text fields) in the future.

| File | Purpose |
|---|---|
| `MainView.java` | Shows login screen, dashboards, resource catalog, and request list |

The view should **never** contain business logic. It only displays data and
calls methods on the controller when the user does something.

---

### `controller/` — Application Logic

The **controller** package sits between the view and the model. It decides what
happens when the user clicks a button — it reads from or writes to the models and
tells the view what to display next.

| File | Purpose |
|---|---|
| `MainController.java` | Handles login, resource operations, request creation, volunteer assignment |

---

### `util/` — Shared Constants

The **util** package holds values that never change and are used across the whole project.
Using constants prevents typos and keeps status values consistent.

| File | Purpose |
|---|---|
| `AppConstants.java` | Role names, resource status values, request status values, request types |

**Rule:** Never use a "magic string" like `"AVAILABLE"` directly in your code.
Always write `AppConstants.RESOURCE_STATUS_AVAILABLE` instead.

---

### `Main.java` — Entry Point

`Main.java` is the first file Java runs. It creates the view and controller and
launches the application. It should contain **minimal code** — just wiring.

---

## Where to Create New Files

| Type of file | Where to put it |
|---|---|
| A new data class (e.g. `Event.java`) | `src/model/` |
| A new screen or dialog (e.g. `LoginDialog.java`) | `src/view/` |
| A new controller (e.g. `ResourceController.java`) | `src/controller/` |
| A new helper/utility class | `src/util/` |
| A new documentation file | `docs/` |

> **Important:** Before creating a new shared class, tell the other team members
> so nobody creates the same class independently.

---

## Inheritance Diagram

```
User  (base class)
 ├── Student
 ├── Volunteer
 ├── Staff
 └── Admin
```

`Student`, `Volunteer`, `Staff`, and `Admin` all inherit the fields from `User`:
`userId`, `name`, `email`, `phone`, `password`, `role`.

They only add fields that are unique to their type.

