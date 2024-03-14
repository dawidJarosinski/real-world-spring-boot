package org.example.realworldspringboot.model.repo;

import org.example.realworldspringboot.model.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepo extends JpaRepository<UserFollow, Integer> {
}
