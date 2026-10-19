# Ashish K Biju — Developer Guide

**Role:** Volunteer Management · Assistance & Emergency Requests · Volunteer Matching

---

## 1 · Your Objective

You manage everything related to **volunteers** and **requests** in CampusPulse.
This includes registering volunteers, tracking their availability, letting users
submit assistance or emergency requests, matching a request to the best available
volunteer, and updating request status as work progresses.

---

## 2 · Files You Own

| File | Your section |
|---|---|
| `src/model/Volunteer.java` | Volunteer fields, `addAssignedRequest()`, `removeAssignedRequest()` |
| `src/model/Request.java` | Request fields, `updateRequestStatus()`, `assignVolunteer()` |
| `src/controller/MainController.java` | **Section 3 — Request & Volunteer Management** |

---

## 3 · Variables and Methods You Will Use

### Fields in `Volunteer.java` (you maintain these)
```java
private String       volunteerId;         // unique ID, e.g. "VOL-001"
private String       availabilityStatus;  // use AppConstants.VOLUNTEER_*
private List<String> assignedRequestIds;  // list of request IDs this volunteer handles
```

### Fields in `Request.java` (you maintain these)
```java
private String requestId;               // unique ID, e.g. "REQ-001"
private String requestType;             // AppConstants.REQUEST_TYPE_*
private String requesterId;             // userId of the person who submitted the request
private String description;             // what help is needed
private String status;                  // AppConstants.REQUEST_STATUS_*
private String assignedVolunteerId;     // volunteerId handling this request (nullable)
private String createdAt;              // ISO timestamp string, e.g. "2026-10-19T15:00:00"
```

### Status and type constants (from `AppConstants.java`)
```java
// Volunteer availability
AppConstants.VOLUNTEER_AVAILABLE    // "AVAILABLE"
AppConstants.VOLUNTEER_BUSY         // "BUSY"
AppConstants.VOLUNTEER_UNAVAILABLE  // "UNAVAILABLE"

// Request status
AppConstants.REQUEST_STATUS_OPEN        // "OPEN"
AppConstants.REQUEST_STATUS_ASSIGNED    // "ASSIGNED"
AppConstants.REQUEST_STATUS_RESOLVED    // "RESOLVED"
AppConstants.REQUEST_STATUS_CANCELLED   // "CANCELLED"

// Request types
AppConstants.REQUEST_TYPE_BORROW        // "BORROW"
AppConstants.REQUEST_TYPE_EMERGENCY     // "EMERGENCY"
AppConstants.REQUEST_TYPE_GENERAL       // "GENERAL"
```

### Methods you will implement in `MainController.java` (Section 3)
```java
public Request    createRequest(String requestType, String requesterId, String description)
public boolean    assignVolunteer(String requestId, String volunteerId)
public void       updateRequestStatus(String requestId, String newStatus)
public Volunteer  matchVolunteer(Request request)
```

### Methods you will implement in `Volunteer.java`
```java
public void addAssignedRequest(String requestId)
public void removeAssignedRequest(String requestId)
```

### Methods you will implement in `Request.java`
```java
public void updateRequestStatus(String newStatus)
public void assignVolunteer(String volunteerId)
```

---

## 4 · What You Should Implement Later

- [ ] **`createRequest()`** — Build a new `Request` object, set `status = OPEN`, record
      `createdAt` as the current time. Store it in a list. If type is `EMERGENCY`,
      immediately call `matchVolunteer()`.
- [ ] **`matchVolunteer(request)`** — Simple matching rule for now:
  - Loop through all volunteers.
  - Find the first one where `availabilityStatus == VOLUNTEER_AVAILABLE`.
  - Return that volunteer (or `null` if none found).
- [ ] **`assignVolunteer(requestId, volunteerId)`** — Find the request and volunteer by
      ID, call `request.assignVolunteer(volunteerId)`, update request status to
      `ASSIGNED`, set volunteer status to `BUSY`, add the request ID to
      `volunteer.assignedRequestIds`.
- [ ] **`updateRequestStatus(requestId, newStatus)`** — Find the request by ID, set its
      status. If `newStatus == RESOLVED`, set the volunteer's status back to `AVAILABLE`
      and remove the request from `volunteer.assignedRequestIds`.
- [ ] **Volunteer registration** — Allow a user to be upgraded to a volunteer by
      creating a `Volunteer` record and linking it to the existing `userId`.

---

## 5 · Who You Depend On

| Dependency | Provided by |
|---|---|
| `User` model (volunteers are users) | Ananya Raju |
| `AppConstants` status and type strings | Ananya Raju |
| `MainView.showVolunteerDashboard()` and `showRequestList()` | Ananya Raju |
| `Resource` model (for BORROW request types) | Ardra K Gireesh |
| `login()` to know who the current user is | Alan Saji |

---

## 6 · Your Task Checklist

```
☐ Read through Volunteer.java and Request.java — understand all fields
☐ Implement createRequest() — create and store a new Request
☐ Implement matchVolunteer() — find first AVAILABLE volunteer
☐ Implement assignVolunteer() — link volunteer to request, update statuses
☐ Implement updateRequestStatus() — handle OPEN → ASSIGNED → RESOLVED transitions
☐ Implement addAssignedRequest() in Volunteer.java
☐ Implement removeAssignedRequest() in Volunteer.java
☐ Coordinate with Ananya so the request list and volunteer dashboard show live data
☐ (Later) Improve matchVolunteer() with better rules (skill match, proximity, etc.)
☐ (Later) Add emergency request prioritization
```

---

## 7 · Files You Should NOT Modify Without Discussion

| File | Why |
|---|---|
| `src/model/User.java` | Shared base class — owned by Ananya |
| `src/util/AppConstants.java` | Shared constants — owned by Ananya |
| `src/model/Resource.java` | Owned by Ardra — use it but don't change its fields |
| `src/view/MainView.java` | Owned by Ananya — do not touch |
| `src/model/Admin.java` | Owned by Alan — do not touch |
| `src/controller/MainController.java` Sections 1, 2, 4, 5 | Other owners' sections |

