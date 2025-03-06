package com.example.capstone.repository;

import com.example.capstone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // Recupero utente tramite username, email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail( String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
