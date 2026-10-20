# Ardra K Gireesh: Resource Catalog, Borrowing, Handover (Parts 4, 5, 6)

## What works
- Search resources in three ways: by keyword, by keyword and category,
  and by keyword, category and "available only".
- Add, find, edit and remove resources. Only the owner can edit or remove.
- Borrow requests: submit, accept, decline, cancel.
- Conflict rule: an item cannot be accepted for more people than its quantity.
- Handover: the owner generates a 4-digit one-time code, and the borrower
  enters it. A wrong or reused code is refused.
- Valid status changes only, with a history of every change.

## Request flow
PENDING -> ACCEPTED -> COMPLETED
PENDING -> DECLINED, and PENDING or ACCEPTED -> CANCELLED

## Files
| File | Job |
|---|---|
| model/ResourceRequest.java | A borrow request. Extends Request. |
| model/StatusHistory.java | One recorded status change. |
| interfaces/Verifiable.java | Contract: generateCode, verify. |
| interfaces/Trackable.java | Contract: canMoveTo, updateStatus, getHistory. |
| service/ResourceService.java | Catalog logic: search, add, edit, remove. |
| service/RequestService.java | Borrow request logic. |
| service/HandoverService.java | One-time code, signs Verifiable. |
| service/StatusTracker.java | Status rules and history, signs Trackable. |
| controller/MainController.java | Section 2 passes work to ResourceService. |

## OOP concepts
- Encapsulation: private fields with getters and setters (Resource, ResourceRequest).
- Inheritance: ResourceRequest extends Request.
- Method overloading: three searchResources methods.
- Interfaces: Verifiable and Trackable, signed by HandoverService and StatusTracker.
- Composition: HandoverService has a RequestService and a ResourceService.
  MainController has a ResourceService.

## Security
The handover code is never stored. Only a SHA-256 hash of "requestId:code" is
kept, and it is erased after one successful use.

## Limits
- Data lives in memory. It is lost on restart, until Alan's database is connected.
- A real app would use a 6-digit code and lock after a few wrong tries.