package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.model.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepo extends JpaRepository<UserFollow, Integer> {
    Boolean existsByFollowerAndFollowed(User follower, User followed);
    Optional<UserFollow> findUserFollowByFollowerAndFollowed(User follower, User followed);
}
