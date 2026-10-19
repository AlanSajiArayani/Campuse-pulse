# CampusPulse — Team Responsibilities

## Team Members

| Member | Role in Project |
|---|---|
| **Alan Saji** | Authentication · Database integration · Admin panel · Final integration & testing |
| **Ananya Raju** | Project structure · OOP models · Java Swing UI · Dashboard design |
| **Ardra K Gireesh** | Resource catalog · Borrowing workflow · Handover tracking |
| **Ashish K Biju** | Volunteer management · Emergency requests · Volunteer matching |

---

## Ownership Map

### Files Owned by Each Member

```
Alan Saji
  ├── src/model/Admin.java          (admin-specific methods)
  └── src/controller/MainController.java  (Section 1: Auth, Section 4: Admin)

Ananya Raju
  ├── src/model/*.java              (OOP model structure — all shared models)
  ├── src/view/MainView.java        (all UI screens)
  └── src/controller/MainController.java  (Section 5: routing/startup)

Ardra K Gireesh
  ├── src/model/Resource.java       (resource-specific methods)
  └── src/controller/MainController.java  (Section 2: Resource management)

Ashish K Biju
  ├── src/model/Volunteer.java      (volunteer-specific methods)
  ├── src/model/Request.java        (request-specific methods)
  └── src/controller/MainController.java  (Section 3: Request & volunteer)
```

> **Shared model fields** (`User.java`, field names, `AppConstants.java`) are owned
> by **Ananya Raju** as the model coordinator. Any change to a shared field must be
> discussed with the whole team before being made.

---

## How the Work Connects

```
Alan Saji                Ananya Raju
(Authentication)  ──▶   (UI screens display the logged-in user)
      │
      ▼
currentUser set in MainController
      │
      ├──▶ Ardra K Gireesh             Ananya Raju
      │    (resource catalog data) ──▶ (resource catalog UI screen)
      │
      └──▶ Ashish K Biju              Ananya Raju
           (volunteer & request) ──▶  (request list & volunteer dashboard UI)
```

### Key Dependencies

| Who needs what | Provided by |
|---|---|
| UI screens need a logged-in user | Alan Saji (`login()`) |
| Resource catalog screen needs resource data | Ardra K Gireesh (`searchResources()`) |
| Request list screen needs request data | Ashish K Biju (`createRequest()`) |
| Volunteer dashboard needs volunteer status | Ashish K Biju (volunteer availability) |
| Admin panel needs user list | Alan Saji (`getAllUsers()`) |

---

## Communication Rules

1. **Before changing a shared field** (in `User.java`, `Resource.java`, `Request.java`,
   or `AppConstants.java`) — post a message to the team group chat first.

2. **Before creating a new class** — check with Ananya Raju (model coordinator) to
   avoid duplicates.

3. **Each member works on their own feature branch.** See `GIT_WORKFLOW.md` for details.

4. **PRs (pull requests) that touch shared files** require at least one other member to
   review before merging.

---

## Checklist: Who Does What First?

The recommended order to avoid blocking each other:

```
Week 1
  ✅ Ananya  — Finalise shared model classes and field names
  ✅ Ananya  — Set up AppConstants with all status/role strings

Week 2
  ☐ Alan    — Implement login/logout in MainController
  ☐ Ardra   — Implement addResource() and searchResources()
  ☐ Ashish  — Implement createRequest() and matchVolunteer()
  ☐ Ananya  — Build login screen and main dashboard UI

Week 3+
  ☐ Alan    — Admin panel, user management
  ☐ Ardra   — Borrowing workflow and handover tracking
  ☐ Ashish  — Emergency request matching and status updates
  ☐ Ananya  — All remaining UI screens

Final Week
  ☐ Alan    — Integration, bug fixes, final testing
```

