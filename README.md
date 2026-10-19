# CampusPulse

A campus resource and volunteer management system built in Java.

---

## What Is This?

CampusPulse is a group project that lets students borrow campus resources, submit
assistance or emergency requests, and get matched with available volunteers. Staff
can contribute items to the resource catalog, and admins manage users and reports.

---

## Team

| Member | Role |
|---|---|
| **Alan Saji** | Authentication, admin panel, database, final integration |
| **Ananya Raju** | Project structure, OOP models, UI, dashboard |
| **Ardra K Gireesh** | Resource catalog, borrowing, handover tracking |
| **Ashish K Biju** | Volunteer management, emergency requests, matching |

---

## Folder Structure

```
Campuse-pulse/
├── src/
│   ├── model/          ← Data classes (User, Student, Resource, Request, …)
│   ├── view/           ← UI screens (MainView.java)
│   ├── controller/     ← Application logic (MainController.java)
│   ├── util/           ← Shared constants (AppConstants.java)
│   └── Main.java       ← Application entry point
├── docs/               ← All project documentation
├── .gitignore
└── README.md
```

---

## Documentation

| Document | Description |
|---|---|
| [PROJECT_OVERVIEW.md](docs/PROJECT_OVERVIEW.md) | What CampusPulse does and how it works end-to-end |
| [PROJECT_STRUCTURE.md](docs/PROJECT_STRUCTURE.md) | Every folder and file explained in plain English |
| [TEAM_RESPONSIBILITIES.md](docs/TEAM_RESPONSIBILITIES.md) | Who owns what and how the work connects |
| [SHARED_VARIABLES.md](docs/SHARED_VARIABLES.md) | Every shared field, type, constant, and naming rule |
| [GIT_WORKFLOW.md](docs/GIT_WORKFLOW.md) | How to branch, commit, push, and open a pull request |
| [ALAN_GUIDE.md](docs/ALAN_GUIDE.md) | Alan's personal developer guide |
| [ANANYA_GUIDE.md](docs/ANANYA_GUIDE.md) | Ananya's personal developer guide |
| [ARDRA_GUIDE.md](docs/ARDRA_GUIDE.md) | Ardra's personal developer guide |
| [ASHISH_GUIDE.md](docs/ASHISH_GUIDE.md) | Ashish's personal developer guide |

**Start here:** Read your personal guide first, then read `SHARED_VARIABLES.md` and
`GIT_WORKFLOW.md` before writing any code.

---

## Setup (No Build Tool Required)

**Requirements:** Java 11 or higher. No Maven, Gradle, or database needed.

### Compile

```bash
javac -d out src/util/AppConstants.java src/model/*.java src/view/*.java src/controller/*.java src/Main.java
```

### Run

```bash
java -cp out Main
```

---

## Conflict Prevention Rules

- **Never** commit directly to `main` — always use a feature branch.
- **Never** rename a shared field without telling the whole team.
- **Never** duplicate a field that already exists in `User.java`.
- **Always** use `AppConstants.*` values instead of hardcoded strings.
- See [GIT_WORKFLOW.md](docs/GIT_WORKFLOW.md) for the full process.
