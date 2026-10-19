# CampusPulse — Shared Variables Reference

> **Rule:** No team member may independently rename a shared field, change its data
> type, or delete it. If a change is necessary, post in the group chat, get everyone's
> agreement, and let Ananya Raju (model coordinator) make the edit.

---

## 1 · User (base class) — `src/model/User.java`

All subclasses (`Student`, `Volunteer`, `Staff`, `Admin`) **inherit** these fields.
Do NOT redeclare them in any subclass.

| Field | Java Type | Purpose |
|---|---|---|
| `userId` | `String` | Unique identifier for any user. Format: `"USR-001"` |
| `name` | `String` | Full display name of the user |
| `email` | `String` | Login email address (must be unique) |
| `phone` | `String` | Contact phone number |
| `password` | `String` | Stored as a hash — **never store plaintext** |
| `role` | `String` | User's system role. Use `AppConstants.ROLE_*` values |

---

## 2 · Student — `src/model/Student.java` (extends User)

| Field | Java Type | Purpose |
|---|---|---|
| `studentId` | `String` | Student-specific ID. Format: `"STU-001"` |
| `department` | `String` | Academic department, e.g. `"Computer Science"` |
| `year` | `int` | Academic year: `1`, `2`, `3`, or `4` |

---

## 3 · Volunteer — `src/model/Volunteer.java` (extends User)

| Field | Java Type | Purpose |
|---|---|---|
| `volunteerId` | `String` | Volunteer-specific ID. Format: `"VOL-001"` |
| `availabilityStatus` | `String` | Current availability. Use `AppConstants.VOLUNTEER_*` |
| `assignedRequestIds` | `List<String>` | IDs of requests currently assigned to this volunteer |

---

## 4 · Staff — `src/model/Staff.java` (extends User)

| Field | Java Type | Purpose |
|---|---|---|
| `staffId` | `String` | Staff-specific ID. Format: `"STF-001"` |
| `department` | `String` | Department the staff member belongs to |

---

## 5 · Admin — `src/model/Admin.java` (extends User)

| Field | Java Type | Purpose |
|---|---|---|
| `adminId` | `String` | Admin-specific ID. Format: `"ADM-001"` |

---

## 6 · Resource — `src/model/Resource.java`

| Field | Java Type | Purpose |
|---|---|---|
| `resourceId` | `String` | Unique resource ID. Format: `"RES-001"` |
| `resourceName` | `String` | Display name, e.g. `"Oscilloscope"` |
| `description` | `String` | Short description of the item |
| `category` | `String` | Category tag, e.g. `"Electronics"`, `"Books"`, `"Sports"` |
| `ownerId` | `String` | `userId` of the staff/admin who manages this resource |
| `quantity` | `int` | Number of available units (must be ≥ 0) |
| `status` | `String` | Availability. Use `AppConstants.RESOURCE_STATUS_*` |

---

## 7 · Request — `src/model/Request.java`

| Field | Java Type | Purpose |
|---|---|---|
| `requestId` | `String` | Unique request ID. Format: `"REQ-001"` |
| `requestType` | `String` | Type of request. Use `AppConstants.REQUEST_TYPE_*` |
| `requesterId` | `String` | `userId` of the person who submitted the request |
| `description` | `String` | Human-readable description of what is needed |
| `status` | `String` | Lifecycle stage. Use `AppConstants.REQUEST_STATUS_*` |
| `assignedVolunteerId` | `String` | `volunteerId` of the assigned volunteer, or `null` |
| `createdAt` | `String` | ISO-8601 timestamp: `"2026-10-19T15:00:00"` |

---

## 8 · AppConstants — `src/util/AppConstants.java`

### Role Values

| Constant | Value | Use when |
|---|---|---|
| `ROLE_STUDENT` | `"STUDENT"` | Setting or comparing a student user's role |
| `ROLE_VOLUNTEER` | `"VOLUNTEER"` | Setting or comparing a volunteer user's role |
| `ROLE_STAFF` | `"STAFF"` | Setting or comparing a staff user's role |
| `ROLE_ADMIN` | `"ADMIN"` | Setting or comparing an admin user's role |

### Resource Status Values

| Constant | Value | Meaning |
|---|---|---|
| `RESOURCE_STATUS_AVAILABLE` | `"AVAILABLE"` | Item can be borrowed |
| `RESOURCE_STATUS_BORROWED` | `"BORROWED"` | Item is currently checked out |
| `RESOURCE_STATUS_UNAVAILABLE` | `"UNAVAILABLE"` | Item is damaged or withheld |

### Request Status Values

| Constant | Value | Meaning |
|---|---|---|
| `REQUEST_STATUS_OPEN` | `"OPEN"` | Request submitted, no volunteer yet |
| `REQUEST_STATUS_ASSIGNED` | `"ASSIGNED"` | A volunteer has been assigned |
| `REQUEST_STATUS_RESOLVED` | `"RESOLVED"` | Request has been fulfilled |
| `REQUEST_STATUS_CANCELLED` | `"CANCELLED"` | Request was cancelled |

### Request Type Values

| Constant | Value | Meaning |
|---|---|---|
| `REQUEST_TYPE_BORROW` | `"BORROW"` | A resource borrow request |
| `REQUEST_TYPE_EMERGENCY` | `"EMERGENCY"` | An urgent help request |
| `REQUEST_TYPE_GENERAL` | `"GENERAL"` | A general assistance request |

### Volunteer Availability Values

| Constant | Value | Meaning |
|---|---|---|
| `VOLUNTEER_AVAILABLE` | `"AVAILABLE"` | Ready to take a new request |
| `VOLUNTEER_BUSY` | `"BUSY"` | Currently handling a request |
| `VOLUNTEER_UNAVAILABLE` | `"UNAVAILABLE"` | Not accepting requests |

---

## 9 · ID Naming Conventions

All IDs are `String` type. Use the following prefixes for easy identification:

| Entity | Prefix | Example |
|---|---|---|
| User (generic) | `USR-` | `"USR-001"` |
| Student | `STU-` | `"STU-042"` |
| Volunteer | `VOL-` | `"VOL-007"` |
| Staff | `STF-` | `"STF-003"` |
| Admin | `ADM-` | `"ADM-001"` |
| Resource | `RES-` | `"RES-010"` |
| Request | `REQ-` | `"REQ-025"` |

IDs will be generated programmatically in a future step. For now use plain strings.

---

## 10 · Zero-Duplication Rule

```
❌ DO NOT add a field to Student, Volunteer, Staff, or Admin
   if that field already exists in User.

❌ DO NOT create a second Resource class or Request class.

❌ DO NOT hardcode status strings like "AVAILABLE" directly in code.
   Always use AppConstants.RESOURCE_STATUS_AVAILABLE.

✅ If you need a brand-new shared field, add it to the correct model class
   and announce the change in the team group chat before pushing.
```

