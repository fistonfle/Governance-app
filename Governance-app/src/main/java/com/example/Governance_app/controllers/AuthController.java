package com.example.Governance_app.controllers;

import com.example.Governance_app.dtos.AuthRequest;
import com.example.Governance_app.dtos.LoginResponse;
import com.example.Governance_app.dtos.SignupRequest;
import com.example.Governance_app.dtos.UserDTO;
import com.example.Governance_app.models.PasswordResetToken;
import com.example.Governance_app.models.User;
import com.example.Governance_app.repositories.PasswordResetTokenRepository;
import com.example.Governance_app.repositories.UserRepository;
import com.example.Governance_app.services.AuthService;
import com.example.Governance_app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private final AuthService authService;

    private final UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthController(AuthService authService, UserRepository userRepository, PasswordResetTokenRepository tokenRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtUtil.generateToken(authRequest.getUsername());

            // Fetch user details
            User user = userRepository.findByUsername(authRequest.getUsername());

            // Map User to UserDTO
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());

            // Prepare response
            return ResponseEntity.ok(new LoginResponse(userDTO, token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String result = authService.registerUser(signupRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username) {
        authService.createPasswordResetToken(username);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Token is no longer needed

        return ResponseEntity.ok("Password successfully reset");
    }


}

