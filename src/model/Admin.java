package model;

import java.util.List;

public class Admin extends User {

    private String adminId;
    private UserRepository userRepository;

    public Admin() {
        super();
    }

    public Admin(String userId, String name, String email, String phone,
                 String password, String adminId) {
        super(userId, name, email, phone, password, "ADMIN");
        this.adminId = adminId;
    }

    public Admin(String userId, String name, String email, String phone,
                 String password, String adminId, UserRepository userRepository) {
        super(userId, name, email, phone, password, "ADMIN");
        this.adminId = adminId;
        this.userRepository = userRepository;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void viewAllUsers() {
        if (userRepository != null) {
            List<User> users = userRepository.getAllUsers();
            for (User u : users) {
                System.out.println(u);
            }
        }
    }

    public void deactivateUser(String userId) {
        if (userRepository != null && userId != null && !userId.trim().isEmpty()) {
            userRepository.deactivateUser(userId);
        }
    }

    public void activateUser(String userId) {
        if (userRepository != null && userId != null && !userId.trim().isEmpty()) {
            userRepository.activateUser(userId);
        }
    }

    @Override
    public String toString() {
        return "Admin{adminId='" + adminId + "', name='" + getName() + "'}";
    }
}

