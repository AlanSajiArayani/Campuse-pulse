package controller;

import model.User;
import model.UserRepository;

public class AuthController {

    private final UserRepository userRepository;
    private User currentUser;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        if (user.getPassword() == null ||
            !user.getPassword().equals(password)) {
            return false;
        }

        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }
}
