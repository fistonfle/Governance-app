package com.example.Governance_app.controllers;

import com.example.Governance_app.dtos.SignupRequest;
import com.example.Governance_app.models.User;
import com.example.Governance_app.services.UserService;
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

    @PutMapping("/changeRole")
    public ResponseEntity<String> changeRole(@RequestBody SignupRequest signupRequest) {
        try {
            String result = userService.changeRole(signupRequest.getUsername(), signupRequest.getRole());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody SignupRequest signupRequest) {
        try {
            String result = userService.deleteUser(signupRequest.getUsername());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestBody SignupRequest signupRequest) {
        try {
            User result = userService.getUser(signupRequest.getUsername());
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
