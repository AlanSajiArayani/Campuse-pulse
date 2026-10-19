package model;

/**
 * User.java
 * Base class representing any user of the CampusPulse system.
 *
 * TODO [Ananya Raju]  – OOP model ownership; ensure sub-classes extend this correctly
 * TODO [Alan Saji]    – Wire authentication logic (login/logout) into this model
 *
 * SHARED MODEL – Do NOT duplicate fields in sub-classes.
 * If a new shared field is needed, add it HERE and notify the team.
 */
public class User {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;   // TODO [Alan Saji] – store hashed password, never plaintext
    private String role;       // Use AppConstants.ROLE_* values

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public User() {}

    /** Full constructor. */
    public User(String userId, String name, String email, String phone,
                String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
        this.role     = role;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getUserId()               { return userId; }
    public void   setUserId(String userId)  { this.userId = userId; }

    public String getName()                 { return name; }
    public void   setName(String name)      { this.name = name; }

    public String getEmail()                { return email; }
    public void   setEmail(String email)    { this.email = email; }

    public String getPhone()                { return phone; }
    public void   setPhone(String phone)    { this.phone = phone; }

    public String getPassword()                   { return password; }
    public void   setPassword(String password)    { this.password = password; }

    public String getRole()               { return role; }
    public void   setRole(String role)    { this.role = role; }

    // -------------------------------------------------------------------------
    // Authentication placeholders
    // TODO [Alan Saji] – Implement login / logout in the controller layer;
    //                    these stubs mark the intended contract.
    // -------------------------------------------------------------------------

    /**
     * Validates credentials and returns {@code true} if authentication succeeds.
     * TODO [Alan Saji] – Implement credential verification
     */
    public boolean login(String email, String password) {
        // TODO [Alan Saji] – Implement
        return false;
    }

    /**
     * Clears the user session.
     * TODO [Alan Saji] – Implement session teardown
     */
    public void logout() {
        // TODO [Alan Saji] – Implement
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name +
               "', email='" + email + "', role='" + role + "'}";
    }
}

