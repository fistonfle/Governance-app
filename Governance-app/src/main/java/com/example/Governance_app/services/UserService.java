package com.example.Governance_app.services;

import com.example.Governance_app.models.User;
import com.example.Governance_app.repositories.UserRepository;
import com.example.Governance_app.utils.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // change role
    public String changeRole(String username, UserRole role) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        user.setRole(role);
        userRepository.save(user);
        return "Role changed successfully!";
    }

    // delete user
    public String deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        userRepository.delete(user);
        return "User deleted successfully!";
    }

    // get user
    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return user;
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
