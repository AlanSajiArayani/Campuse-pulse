package model;

/**
 * Request.java
 * Represents a service or emergency request raised by a user.
 *
 * TODO [Ashish K Biju]  – Volunteer matching, emergency request workflow
 * TODO [Ardra K Gireesh] – Borrow-type requests link to Resource catalog
 *
 * SHARED MODEL – Do NOT duplicate fields or create a parallel request class.
 */
public class Request {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String requestId;
    private String requestType;            // Use AppConstants.REQUEST_TYPE_* values
    private String requesterId;            // userId of the person who raised the request
    private String description;
    private String status;                 // Use AppConstants.REQUEST_STATUS_* values
    private String assignedVolunteerId;    // volunteerId assigned to this request (nullable)
    private String createdAt;             // ISO-8601 timestamp string, e.g. "2026-10-19T15:00:00"

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Request() {}

    /** Full constructor. */
    public Request(String requestId, String requestType, String requesterId,
                   String description, String status,
                   String assignedVolunteerId, String createdAt) {
        this.requestId             = requestId;
        this.requestType           = requestType;
        this.requesterId           = requesterId;
        this.description           = description;
        this.status                = status;
        this.assignedVolunteerId   = assignedVolunteerId;
        this.createdAt             = createdAt;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getRequestId()                  { return requestId; }
    public void   setRequestId(String requestId)  { this.requestId = requestId; }

    public String getRequestType()                    { return requestType; }
    public void   setRequestType(String requestType)  { this.requestType = requestType; }

    public String getRequesterId()                    { return requesterId; }
    public void   setRequesterId(String requesterId)  { this.requesterId = requesterId; }

    public String getDescription()                    { return description; }
    public void   setDescription(String description)  { this.description = description; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    public String getAssignedVolunteerId()                          { return assignedVolunteerId; }
    public void   setAssignedVolunteerId(String assignedVolunteerId) { this.assignedVolunteerId = assignedVolunteerId; }

    public String getCreatedAt()                  { return createdAt; }
    public void   setCreatedAt(String createdAt)  { this.createdAt = createdAt; }

    // -------------------------------------------------------------------------
    // Request-specific method placeholders
    // -------------------------------------------------------------------------

    /**
     * Updates the status of this request.
     * TODO [Ashish K Biju] – Implement status transition logic and validation
     *
     * @param newStatus the new status value (use AppConstants.REQUEST_STATUS_*)
     */
    public void updateRequestStatus(String newStatus) {
        // TODO [Ashish K Biju] – Implement
    }

    /**
     * Assigns a volunteer to this request.
     * TODO [Ashish K Biju] – Implement volunteer assignment and matching
     *
     * @param volunteerId the volunteerId to assign
     */
    public void assignVolunteer(String volunteerId) {
        // TODO [Ashish K Biju] – Implement
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Request{requestId='" + requestId + "', requestType='" + requestType +
               "', requesterId='" + requesterId + "', status='" + status +
               "', assignedVolunteerId='" + assignedVolunteerId + "'}";
    }
}

