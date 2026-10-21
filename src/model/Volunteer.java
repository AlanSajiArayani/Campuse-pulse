package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Volunteer.java
 * Represents a volunteer user of CampusPulse.
 * Extends {@link User} — inherits userId, name, email, phone, password, role.
 *
 * TODO [Ashish K Biju] – Volunteer management, emergency request matching
 *
 * SHARED MODEL – Do NOT add fields that already exist in User.
 */
public class Volunteer extends User {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String       volunteerId;
    private String       availabilityStatus;   // Use AppConstants.VOLUNTEER_* values
    private List<String> assignedRequestIds;   // IDs of requests assigned to this volunteer

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Volunteer() {
        super();
        this.assignedRequestIds = new ArrayList<>();
    }

    /** Full constructor. */
    public Volunteer(String userId, String name, String email, String phone,
                     String password, String volunteerId, String availabilityStatus) {
        super(userId, name, email, phone, password, "VOLUNTEER");
        this.volunteerId        = volunteerId;
        this.availabilityStatus = availabilityStatus;
        this.assignedRequestIds = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getVolunteerId()                    { return volunteerId; }
    public void   setVolunteerId(String volunteerId)  { this.volunteerId = volunteerId; }

    public String getAvailabilityStatus()                           { return availabilityStatus; }
    public void   setAvailabilityStatus(String availabilityStatus)  { this.availabilityStatus = availabilityStatus; }

    public List<String> getAssignedRequestIds()                           { return assignedRequestIds; }
    public void         setAssignedRequestIds(List<String> requestIds)    { this.assignedRequestIds = requestIds; }

    public void addAssignedRequest(String requestId) {
    if (requestId != null && !assignedRequestIds.contains(requestId)) {
        assignedRequestIds.add(requestId);
    }
}

public void removeAssignedRequest(String requestId) {
    assignedRequestIds.remove(requestId);
}

    @Override
    public String toString() {
        return "Volunteer{volunteerId='" + volunteerId + "', name='" + getName() +
               "', availabilityStatus='" + availabilityStatus +
               "', assignedRequests=" + assignedRequestIds.size() + "}";
    }
}

