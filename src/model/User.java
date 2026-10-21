package model;

/**
 * User.java
 * Base domain model representing a registered user in CampusPulse.
 * Subclassed by Student, Staff, Volunteer, and Admin.
 */
public class User {

    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    public User() {}

    public User(String userId, String name, String email, String phone,
                String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
        this.role     = role;
    }

    public String getUserId()              { return userId; }
    public void   setUserId(String userId) { this.userId = userId; }

    public String getName()                { return name; }
    public void   setName(String name)     { this.name = name; }

    public String getEmail()               { return email; }
    public void   setEmail(String email)   { this.email = email; }

    public String getPhone()               { return phone; }
    public void   setPhone(String phone)   { this.phone = phone; }

    public String getPassword()                  { return password; }
    public void   setPassword(String password)   { this.password = password; }

    public String getRole()              { return role; }
    public void   setRole(String role)   { this.role = role; }

    /**
     * Validates credentials against this user instance.
     */
    public boolean login(String email, String password) {
        return email != null && email.equalsIgnoreCase(this.email)
            && password != null && password.equals(this.password);
    }

    /**
     * Clears user session reference.
     */
    public void logout() {
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name +
               "', email='" + email + "', role='" + role + "'}";
    }
}

