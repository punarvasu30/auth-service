package com.support.auth_service.service;

import com.support.auth_service.model.User;
import com.support.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String rawPassword, String name) {

        // 1. Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // 2. Encode password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 3. Create user entity
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .role(User.Role.valueOf("USER"))
                .authProvider(User.AuthProvider.valueOf("LOCAL"))
                .enabled(true)
                .build();

        // 4. Save user
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
