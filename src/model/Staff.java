package model;

/**
 * Staff.java
 * Represents a staff member user of CampusPulse.
 * Extends {@link User} — inherits userId, name, email, phone, password, role.
 *
 * TODO [Ananya Raju] – OOP model ownership
 * TODO [Ardra K Gireesh] – Staff may own resources; link via ownerId in Resource
 *
 * SHARED MODEL – Do NOT add fields that already exist in User.
 */
public class Staff extends User {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String staffId;
    private String department;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Staff() {
        super();
    }

    /** Full constructor. */
    public Staff(String userId, String name, String email, String phone,
                 String password, String staffId, String department) {
        super(userId, name, email, phone, password, "STAFF");
        this.staffId    = staffId;
        this.department = department;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getStaffId()                { return staffId; }
    public void   setStaffId(String staffId)  { this.staffId = staffId; }

    public String getDepartment()                   { return department; }
    public void   setDepartment(String department)  { this.department = department; }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Staff{staffId='" + staffId + "', name='" + getName() +
               "', department='" + department + "'}";
    }
}

