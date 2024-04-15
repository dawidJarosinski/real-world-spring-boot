package org.example.realworldspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.CantFollowYourselfOrAlreadyFollowedException;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.model.entity.UserFollow;
import org.example.realworldspringboot.repo.UserFollowRepo;
import org.example.realworldspringboot.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepo userRepo;
    private final UserFollowRepo userFollowRepo;


    public ProfileResponse findByUsername(String username, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        User user = userRepo.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

        return buildProfileResponse(currentUser, user);
    }

    @Transactional
    public ProfileResponse followUser(String username, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        User userToFollow = userRepo.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

        if(!userFollowRepo.existsByFollowerAndFollowed(currentUser, userToFollow) && !currentUser.equals(userToFollow)) {
            UserFollow userFollow = new UserFollow(currentUser, userToFollow);
            userFollowRepo.save(userFollow);
        } else {
            throw new CantFollowYourselfOrAlreadyFollowedException();
        }

        return buildProfileResponse(currentUser, userToFollow);
    }

    @Transactional
    public ProfileResponse unfollowUser(String username, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        User followedUser = userRepo.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

        Optional<UserFollow> follow = userFollowRepo.findUserFollowByFollowerAndFollowed(currentUser, followedUser);
        follow.ifPresentOrElse(userFollowRepo::delete, () -> {
            throw new UserNotFoundException();
        });

        return buildProfileResponse(currentUser, followedUser);
    }
    private ProfileResponse buildProfileResponse(User currentUser, User user) {
        return ProfileResponse.builder()
                .profile(
                        ProfileResponse.Profile
                                .builder()
                                .username(user.getUsername())
                                .bio(user.getBio())
                                .image(user.getImage())
                                .following(userFollowRepo.existsByFollowerAndFollowed(currentUser, user))
                                .build()
                )
                .build();
    }
}
