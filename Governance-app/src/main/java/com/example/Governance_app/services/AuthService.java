package com.example.Governance_app.services;

import com.example.Governance_app.dtos.SignupRequest;
import com.example.Governance_app.models.PasswordResetToken;
import com.example.Governance_app.models.User;
import com.example.Governance_app.repositories.PasswordResetTokenRepository;
import com.example.Governance_app.repositories.UserRepository;
import com.example.Governance_app.utils.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final PasswordResetTokenRepository tokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public String registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());

        // Set the role, for example:
        user.setRole(UserRole.ROLE_ADMIN); // Or "ROLE_ADMIN" for admin users

        userRepository.save(user);
        return "User registered successfully!";
    }

    public void createPasswordResetToken(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour

        tokenRepository.save(resetToken);

        // Send email with reset link
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "To reset your password, click the link: " + resetLink);
    }
}
