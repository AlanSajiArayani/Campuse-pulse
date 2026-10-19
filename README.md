# CampusPulse

A campus resource and volunteer management system built in Java.

## Team Members & Ownership

| Member | Area of Responsibility |
|---|---|
| **Alan Saji** | Authentication, database integration, admin panel, final integration |
| **Ananya Raju** | Project structure, OOP models, UI and dashboard |
| **Ardra K Gireesh** | Resource catalog, borrowing and handover tracking |
| **Ashish K Biju** | Volunteer management, emergency requests and matching |

## Project Structure

```
CampusPulse/
├── src/
│   ├── model/
│   │   ├── User.java          – Base user model (shared)
│   │   ├── Student.java       – Student sub-model
│   │   ├── Volunteer.java     – Volunteer sub-model
│   │   ├── Staff.java         – Staff sub-model
│   │   ├── Admin.java         – Admin sub-model
│   │   ├── Resource.java      – Resource catalog model
│   │   └── Request.java       – Service/emergency request model
│   ├── view/
│   │   └── MainView.java      – Entry-point UI (Ananya Raju)
│   ├── controller/
│   │   └── MainController.java – Central controller
│   ├── util/
│   │   └── AppConstants.java  – Shared constants
│   └── Main.java              – Application entry point
├── .gitignore
└── README.md
```

## Shared Model Rules

- **Do NOT** add duplicate fields for the same purpose across different classes.
- **Do NOT** create a second version of an existing model class.
- If a new shared field is needed, update the relevant model class and communicate the change to all team members.

## How to Run

> Java 11+ required. No build tool is configured — compile manually.

```bash
# From the project root
javac -d out src/util/AppConstants.java src/model/*.java src/view/*.java src/controller/*.java src/Main.java
java -cp out Main
```

