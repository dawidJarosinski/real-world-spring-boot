package org.example.realworldspringboot.model.repo;

import org.example.realworldspringboot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
