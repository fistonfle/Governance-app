package com.example.Governance_app.controllers;

import com.example.Governance_app.dtos.SignupRequest;
import com.example.Governance_app.models.User;
import com.example.Governance_app.services.UserService;
import com.example.Governance_app.utils.enums.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/changeRole/{username}")
    public ResponseEntity<String> changeRole(@RequestBody UserRole role, @PathVariable String username) {
        try {
            String result = userService.changeRole(username, role);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            String result = userService.deleteUser(username);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUser/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            User result = userService.getUser(username);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> result = userService.getAllUsers();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
