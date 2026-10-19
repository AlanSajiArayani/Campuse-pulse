# Ananya Raju — Developer Guide

**Role:** Project Structure · OOP Models · Java Swing UI · Dashboard Design

---

## 1 · Your Objective

You are the **model coordinator** for the project. You ensure the shared data classes
are correctly designed and that all team members use them consistently. You also build
the visual interface — every screen the user sees is your responsibility.

---

## 2 · Files You Own

| File | Your responsibility |
|---|---|
| `src/model/User.java` | Base user class — field definitions, constructors, getters/setters |
| `src/model/Student.java` | Student subclass |
| `src/model/Staff.java` | Staff subclass |
| `src/view/MainView.java` | All UI screens and dialogs |
| `src/controller/MainController.java` | **Section 5 — Startup/Routing** (`start()`) |
| `src/util/AppConstants.java` | All shared constants |

> You are also the **reviewer** for any pull request that modifies a shared model file.

---

## 3 · Variables and Methods You Will Use

### Fields you maintain in `User.java`
```java
private String userId;
private String name;
private String email;
private String phone;
private String password;
private String role;      // Use AppConstants.ROLE_*
```

### Fields you maintain in `Student.java` (extends User)
```java
private String studentId;
private String department;
private int    year;
```

### Fields you maintain in `Staff.java` (extends User)
```java
private String staffId;
private String department;
```

### Constants you maintain in `AppConstants.java`
```java
ROLE_STUDENT, ROLE_VOLUNTEER, ROLE_STAFF, ROLE_ADMIN
RESOURCE_STATUS_AVAILABLE, RESOURCE_STATUS_BORROWED, RESOURCE_STATUS_UNAVAILABLE
REQUEST_STATUS_OPEN, REQUEST_STATUS_ASSIGNED, REQUEST_STATUS_RESOLVED, REQUEST_STATUS_CANCELLED
REQUEST_TYPE_BORROW, REQUEST_TYPE_EMERGENCY, REQUEST_TYPE_GENERAL
VOLUNTEER_AVAILABLE, VOLUNTEER_BUSY, VOLUNTEER_UNAVAILABLE
```

### Methods you will implement in `MainView.java`
```java
public void showLoginScreen()
public void showStudentDashboard()
public void showVolunteerDashboard()
public void showStaffDashboard()
public void showAdminPanel()
public void showResourceCatalog()
public void showRequestList()
public void showMessage(String message)
public void showError(String error)
```

---

## 4 · What You Should Implement Later

- [ ] **Login screen** — text fields for email/password, a Login button that calls
      `controller.login(email, password)`.
- [ ] **Dashboard routing** — after login, check `currentUser.getRole()` and call the
      correct `show*Dashboard()` method.
- [ ] **Student dashboard** — shows available resources, open requests, borrow button.
- [ ] **Volunteer dashboard** — shows assigned requests, availability toggle.
- [ ] **Staff dashboard** — shows resources they own, option to add/update items.
- [ ] **Admin panel** — user list table, deactivate button.
- [ ] **Resource catalog screen** — search bar, list/table of resources, borrow button.
- [ ] **Request list screen** — list of open requests, status column.

---

## 5 · Who You Depend On

| Dependency | Provided by |
|---|---|
| `login()` returning the logged-in user | Alan Saji |
| Resource data for the catalog screen | Ardra K Gireesh (`searchResources()`) |
| Request data for the request list | Ashish K Biju (`createRequest()`) |
| Volunteer status data | Ashish K Biju (volunteer availability) |

---

## 6 · Your Task Checklist

```
☐ Confirm all shared model fields are correct (User, Student, Staff, AppConstants)
☐ Make sure Student and Staff only add fields NOT already in User
☐ Build the login screen (showLoginScreen)
☐ Wire the Login button to MainController.login()
☐ Implement dashboard routing based on user role
☐ Build the resource catalog screen (showResourceCatalog)
☐ Build the request list screen (showRequestList)
☐ Build role-specific dashboards
☐ Test that all screens display without errors
```

---

## 7 · Important: Shared Model Field Rules

> You are the gatekeeper of shared fields. Enforce these rules with your team:

- **Never** add a field to `Student`, `Volunteer`, `Staff`, or `Admin` that already
  exists in `User` (e.g., do not add `name` or `email` to `Student`).
- **Never** rename an existing field in `User.java` or `AppConstants.java` without
  telling all three other team members first — it will break their code immediately.
- If a new field is genuinely needed, add it to the correct shared class and tell
  the team before pushing.

---

## 8 · Files You Should NOT Modify Without Discussion

| File | Why |
|---|---|
| `src/model/Volunteer.java` | Owned by Ashish — coordinate before touching |
| `src/model/Request.java` | Owned by Ashish — coordinate before touching |
| `src/model/Resource.java` | Owned by Ardra — coordinate before touching |
| `src/model/Admin.java` | Owned by Alan — coordinate before touching |
| `src/controller/MainController.java` Sections 1–4 | Each section has a different owner |

