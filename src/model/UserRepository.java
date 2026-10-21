package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private final Set<String> deactivatedUserIds = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail() != null &&
                user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public User findById(String userId) {
        if (userId == null) {
            return null;
        }
        for (User user : users) {
            if (userId.equals(user.getUserId())) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users));
    }

    public boolean deactivateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        User user = findById(userId);
        if (user == null) {
            return false;
        }
        deactivatedUserIds.add(userId);
        return true;
    }

    public boolean isUserActive(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        User user = findById(userId);
        if (user == null) {
            return false;
        }
        return !deactivatedUserIds.contains(userId);
    }
}
