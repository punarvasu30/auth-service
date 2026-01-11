package com.support.auth_service.repository;


import com.support.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByAuthProviderAndProviderId(String authProvider, String providerId);

    boolean existsByEmail(String email);
}
