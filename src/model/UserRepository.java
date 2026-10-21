package model;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

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
}
