package com.support.auth_service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.support.auth_service.model.User;
import com.support.auth_service.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1. Load user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(()
                        -> new UsernameNotFoundException("User not found: " + email)
                );

        // 2. Convert DB User → Spring Security UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.getEnabled())
                .authorities(user.getRole().name())
                .build();
    }
}
