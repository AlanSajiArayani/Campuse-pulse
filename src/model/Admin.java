package model;

/**
 * Admin.java
 * Represents an administrator user of CampusPulse.
 * Extends {@link User} — inherits userId, name, email, phone, password, role.
 *
 * TODO [Alan Saji] – Admin panel, user management, and final integration
 *
 * SHARED MODEL – Do NOT add fields that already exist in User.
 */
public class Admin extends User {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String adminId;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Admin() {
        super();
    }

    /** Full constructor. */
    public Admin(String userId, String name, String email, String phone,
                 String password, String adminId) {
        super(userId, name, email, phone, password, "ADMIN");
        this.adminId = adminId;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getAdminId()                { return adminId; }
    public void   setAdminId(String adminId)  { this.adminId = adminId; }

    // -------------------------------------------------------------------------
    // Admin-specific method placeholders
    // TODO [Alan Saji] – Implement admin operations below
    // -------------------------------------------------------------------------

    /**
     * Returns a list (or report) of all system users.
     * TODO [Alan Saji] – Implement; fetch from data source
     */
    public void viewAllUsers() {
        // TODO [Alan Saji] – Implement
    }

    /**
     * Deactivates a user account by userId.
     * TODO [Alan Saji] – Implement
     */
    public void deactivateUser(String userId) {
        // TODO [Alan Saji] – Implement
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Admin{adminId='" + adminId + "', name='" + getName() + "'}";
    }
}

