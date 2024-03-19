package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
