# Alan Saji — Developer Guide

**Role:** Authentication · Database Integration · Admin Panel · Final Integration & Testing

---

## 1 · Your Objective

You are responsible for making sure only the right users can access the system
(authentication), managing admin-level operations, and bringing all team members'
work together in the final integration phase.

---

## 2 · Files You Own

| File | Your section |
|---|---|
| `src/model/Admin.java` | `viewAllUsers()`, `deactivateUser()` stubs |
| `src/controller/MainController.java` | **Section 1 — Auth** (`login`, `logout`, `getCurrentUser`) |
| `src/controller/MainController.java` | **Section 4 — Admin** (`getAllUsers`, `deactivateUser`) |
| `src/Main.java` | Pre-startup initialization (when needed) |

---

## 3 · Variables and Methods You Will Use

### From `User.java` (read — do not change fields without discussion)
```java
String userId;
String name;
String email;
String phone;
String password;   // store hashed — never plaintext
String role;       // compare against AppConstants.ROLE_*
```

### From `AppConstants.java`
```java
AppConstants.ROLE_STUDENT
AppConstants.ROLE_VOLUNTEER
AppConstants.ROLE_STAFF
AppConstants.ROLE_ADMIN
```

### Methods you will implement in `MainController.java`
```java
public boolean login(String email, String password)   // Section 1
public void    logout()                               // Section 1
public User    getCurrentUser()                       // Section 1
public List<User> getAllUsers()                       // Section 4
public void    deactivateUser(String userId)          // Section 4
```

### Methods you will implement in `Admin.java`
```java
public void viewAllUsers()
public void deactivateUser(String userId)
```

---

## 4 · What You Should Implement Later

- [ ] **Login logic** — compare email + hashed password against stored users, set
      `currentUser` on success.
- [ ] **Logout logic** — clear `currentUser`, navigate back to the login screen.
- [ ] **Admin: view all users** — retrieve and display all registered users.
- [ ] **Admin: deactivate user** — mark a user as inactive so they cannot log in.
- [ ] **Database integration** (future) — replace in-memory data with file/database
      reads. Keep the method signatures unchanged so other members are unaffected.
- [ ] **Final integration** — merge all branches, run the full application end-to-end,
      fix any remaining issues.

---

## 5 · Who You Depend On

| Dependency | Provided by |
|---|---|
| `User` model fields and `Admin` model structure | Ananya Raju |
| `AppConstants` role strings | Ananya Raju |
| `MainView.showLoginScreen()` and `showAdminPanel()` | Ananya Raju |
| User list to display in admin panel | Your own `getAllUsers()` |

---

## 6 · Your Task Checklist

```
☐ Understand the User model fields (User.java)
☐ Implement login() in MainController — return true/false
☐ Implement logout() in MainController — clear currentUser
☐ Route logged-in user to the correct dashboard (call MainView)
☐ Implement getAllUsers() in MainController
☐ Implement deactivateUser() in MainController and Admin.java
☐ (Later) Replace in-memory data with a file or database
☐ (Final) Run the full application and fix integration issues
```

---

## 7 · Files You Should NOT Modify Without Discussion

| File | Why |
|---|---|
| `src/model/User.java` | Shared base class — changing field names breaks all subclasses |
| `src/util/AppConstants.java` | Shared constants — renaming breaks every class that uses them |
| `src/model/Resource.java` | Owned by Ardra — coordinate before touching |
| `src/model/Request.java` | Owned by Ashish — coordinate before touching |
| `src/model/Volunteer.java` | Owned by Ashish — coordinate before touching |
| `src/view/MainView.java` | Owned by Ananya — coordinate before touching |

If you need a change in any of the above files, create an issue or message the owner
before making the edit.

