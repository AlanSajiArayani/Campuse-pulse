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
    private UserRepository userRepository;

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

    /** Constructor with UserRepository. */
    public Admin(String userId, String name, String email, String phone,
                 String password, String adminId, UserRepository userRepository) {
        super(userId, name, email, phone, password, "ADMIN");
        this.adminId = adminId;
        this.userRepository = userRepository;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getAdminId()                { return adminId; }
    public void   setAdminId(String adminId)  { this.adminId = adminId; }

    public UserRepository getUserRepository()                           { return userRepository; }
    public void           setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }

    // -------------------------------------------------------------------------
    // Admin-specific method placeholders
    // TODO [Alan Saji] – Implement admin operations below
    // -------------------------------------------------------------------------

    /**
     * Displays all registered users from the user repository.
     */
    public void viewAllUsers() {
        if (userRepository != null) {
            java.util.List<User> users = userRepository.getAllUsers();
            for (User u : users) {
                System.out.println(u);
            }
        }
    }

    /**
     * Deactivates a user account by userId.
     */
    public void deactivateUser(String userId) {
        if (userRepository != null && userId != null && !userId.trim().isEmpty()) {
            userRepository.deactivateUser(userId);
        }
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Admin{adminId='" + adminId + "', name='" + getName() + "'}";
    }
}

