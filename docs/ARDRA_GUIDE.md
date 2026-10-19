# Ardra K Gireesh — Developer Guide

**Role:** Resource Catalog · Borrowing Workflow · Quantity & Availability · Handover Tracking

---

## 1 · Your Objective

You build and maintain everything related to **resources** in CampusPulse. This includes
listing available items, letting users search the catalog, processing borrow requests,
updating item quantities, and tracking the handover when an item is returned.

---

## 2 · Files You Own

| File | Your section |
|---|---|
| `src/model/Resource.java` | Resource fields, `borrowResource()`, `returnResource()` |
| `src/controller/MainController.java` | **Section 2 — Resource Management** (`addResource`, `searchResources`, `borrowResource`, `returnResource`) |

---

## 3 · Variables and Methods You Will Use

### Fields in `Resource.java` (you maintain these)
```java
private String resourceId;      // unique ID, e.g. "RES-001"
private String resourceName;    // display name, e.g. "Oscilloscope"
private String description;     // short description
private String category;        // e.g. "Electronics", "Books", "Sports"
private String ownerId;         // userId of the staff/admin who owns this resource
private int    quantity;        // number of available units
private String status;          // use AppConstants.RESOURCE_STATUS_*
```

### Status values to use (from `AppConstants.java`)
```java
AppConstants.RESOURCE_STATUS_AVAILABLE    // "AVAILABLE"
AppConstants.RESOURCE_STATUS_BORROWED     // "BORROWED"
AppConstants.RESOURCE_STATUS_UNAVAILABLE  // "UNAVAILABLE"
```

### Request fields you will read (from `Request.java`, owned by Ashish)
```java
String requestId;
String requestType;     // will be AppConstants.REQUEST_TYPE_BORROW
String requesterId;     // the student borrowing the resource
String status;          // track the borrow request lifecycle
```

### Methods you will implement in `MainController.java` (Section 2)
```java
public void            addResource(Resource resource)
public List<Resource>  searchResources(String keyword)
public boolean         borrowResource(String resourceId, String borrowerId)
public boolean         returnResource(String resourceId, String returnerId)
```

### Methods you will implement in `Resource.java`
```java
public boolean borrowResource(String borrowerId)
public boolean returnResource(String returnerId)
```

---

## 4 · What You Should Implement Later

- [ ] **`addResource()`** — Create a new `Resource` object and store it (in a list for
      now, in a file/database later).
- [ ] **`searchResources(keyword)`** — Loop through the resource list and return items
      whose name or category contains the keyword (case-insensitive).
- [ ] **`borrowResource(resourceId, borrowerId)`** — Find the resource by ID, check
      `quantity > 0` and `status == AVAILABLE`, then:
  - Decrement `quantity`.
  - Set `status = BORROWED` if `quantity == 0`.
  - Create a new `Request` of type `BORROW` (coordinate with Ashish for the method).
  - Return `true` if successful.
- [ ] **`returnResource(resourceId, returnerId)`** — Find the resource, increment
      `quantity`, set `status = AVAILABLE`, update the related `Request` to `RESOLVED`.
- [ ] **Handover tracking** — Keep a record of who borrowed what and when (use
      `Request.createdAt` and a returned-at timestamp).

---

## 5 · Who You Depend On

| Dependency | Provided by |
|---|---|
| `User` model (to identify borrowers by `userId`) | Ananya Raju |
| `Request` model (to create a BORROW request) | Ashish K Biju |
| `AppConstants.RESOURCE_STATUS_*` and `REQUEST_TYPE_BORROW` | Ananya Raju |
| Resource catalog UI screen (`showResourceCatalog()`) | Ananya Raju |
| `createRequest()` method to log borrow events | Ashish K Biju |

---

## 6 · Your Task Checklist

```
☐ Read through Resource.java and understand all fields
☐ Implement addResource() in MainController
☐ Implement searchResources(keyword) — return matching resources
☐ Implement borrowResource() — check availability, decrement quantity
☐ Implement returnResource() — increment quantity, update status
☐ Coordinate with Ashish to create a BORROW Request when borrowing
☐ Coordinate with Ananya to pass resource data to the catalog UI screen
☐ (Later) Add handover timestamps and return tracking
```

---

## 7 · Files You Should NOT Modify Without Discussion

| File | Why |
|---|---|
| `src/model/User.java` | Shared base class — owned by Ananya |
| `src/util/AppConstants.java` | Shared constants — owned by Ananya |
| `src/model/Request.java` | Owned by Ashish — use it but don't change its fields |
| `src/model/Volunteer.java` | Owned by Ashish — do not touch |
| `src/view/MainView.java` | Owned by Ananya — do not touch |
| `src/controller/MainController.java` Sections 1, 3, 4, 5 | Other owners' sections |

