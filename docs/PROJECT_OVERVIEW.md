# CampusPulse — Project Overview

## What is CampusPulse?

CampusPulse is a campus resource and volunteer management system for students, staff,
and volunteers at a college or university. It provides a single place where:

- **Students** can browse available resources, borrow items, and submit requests for
  assistance or emergency help.
- **Volunteers** can see which requests need their help, mark themselves available, and
  update request progress.
- **Staff** can contribute resources to the catalog, approve borrow requests, and
  coordinate with volunteers.
- **Admins** can manage all users, view reports, and keep the system running smoothly.

---

## Core Features

### 1 · Resource Catalog & Borrowing

Campus resources (lab equipment, books, sports gear, tools, etc.) are listed in a
shared catalog. Each item shows its name, category, quantity, and current availability.
Students and staff can search the catalog by keyword or category, and request to borrow
an available item.

When a borrow request is submitted the system:
1. Checks that the item is available (`status = AVAILABLE`) and quantity > 0.
2. Creates a **Request** of type `BORROW`.
3. Changes the resource status to `BORROWED` and decrements the quantity.
4. Tracks the handover until the borrower returns the item.

### 2 · Volunteer Management

Students or staff can register as **Volunteers**. Each volunteer has an availability
status (`AVAILABLE`, `BUSY`, or `UNAVAILABLE`) and a list of requests they are
currently handling.

The system allows a coordinator to:
- View all available volunteers.
- Assign a volunteer to an open request.
- Update volunteer availability when they take on or finish a task.

### 3 · Assistance & Emergency Requests

Any registered user can raise a **Request** (type `GENERAL` or `EMERGENCY`).
Emergency requests are flagged with higher urgency. The system:
1. Records the request with `status = OPEN`.
2. Finds and assigns an available volunteer.
3. Notifies the assigned volunteer.
4. Tracks progress through `ASSIGNED` → `RESOLVED`.

### 4 · Admin Panel

Admins can view all registered users, deactivate accounts, and see a summary of
requests and resource usage across the campus.

---

## Intended Workflow (End-to-End)

```
User logs in
     │
     ├─ Student ──▶ Search resource catalog
     │                    │
     │              Item available?
     │               ├─ Yes ──▶ Submit BORROW request
     │               │              │
     │               │         Volunteer assigned ──▶ Handover tracked ──▶ RESOLVED
     │               │
     │               └─ No  ──▶ Submit GENERAL request ──▶ Volunteer matched ──▶ RESOLVED
     │
     ├─ Volunteer ──▶ View assigned requests ──▶ Mark complete
     │
     ├─ Staff ──▶ Add/update resources in catalog
     │
     └─ Admin ──▶ Manage users and view reports
```

---

## Technology Decisions (Beginner-Friendly)

| Concern | Choice | Reason |
|---|---|---|
| Language | Java | Team familiarity |
| Architecture | MVC (Model-View-Controller) | Clear separation of concerns |
| UI | Java Swing (future) | No web framework needed |
| Data storage | In-memory (future: file/DB) | Keep it simple for now |
| Build tool | None — plain javac | No Maven/Gradle complexity |

---

## Glossary

| Term | Meaning |
|---|---|
| `userId` | Unique string ID for any user in the system |
| `resourceId` | Unique string ID for a resource in the catalog |
| `requestId` | Unique string ID for a submitted request |
| `status` | Current lifecycle stage of a resource or request |
| `ownerId` | `userId` of the staff/admin who manages a resource |
| `requesterId` | `userId` of the user who submitted a request |
| `assignedVolunteerId` | `volunteerId` of the volunteer handling a request |

