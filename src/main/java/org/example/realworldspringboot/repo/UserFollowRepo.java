package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepo extends JpaRepository<UserFollow, Integer> {
}
