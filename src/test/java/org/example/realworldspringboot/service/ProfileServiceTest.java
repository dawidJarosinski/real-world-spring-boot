package org.example.realworldspringboot.service;

import org.example.realworldspringboot.config.exceptions.CantFollowYourselfOrAlreadyFollowedException;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.model.entity.UserFollow;
import org.example.realworldspringboot.repo.UserFollowRepo;
import org.example.realworldspringboot.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserFollowRepo userFollowRepo;

    private ProfileService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProfileService(userRepo, userFollowRepo);
    }

    @Test
    void FindByUsername_InvokeMethod_ShouldReturnProfileResponse() {
        User currentUser = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        User userToFind = User.builder()
                .email("test2@gmail.com")
                .image("image")
                .bio("bio")
                .id(2)
                .password("password")
                .username("username2")
                .build();
        given(userRepo.findUserByUsername(currentUser.getUsername())).willReturn(Optional.of(currentUser));
        given(userRepo.findUserByUsername(userToFind.getUsername())).willReturn(Optional.of(userToFind));
        given(userFollowRepo.existsByFollowerAndFollowed(currentUser, userToFind)).willReturn(true);


        ProfileResponse profileResponse = underTest.findByUsername("username2", "username");

        assertThat(profileResponse.profile().username()).isEqualTo(userToFind.getUsername());
        assertThat(profileResponse.profile().bio()).isEqualTo(userToFind.getBio());
        assertThat(profileResponse.profile().following()).isEqualTo(true);
        assertThat(profileResponse.profile().image()).isEqualTo(userToFind.getImage());
    }

    @Test
    void FollowUser_InvokeFollowUserMethod_ShouldFollowUserAndReturnProfile() {
        User currentUser = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        User userToFollow = User.builder()
                .email("test2@gmail.com")
                .image("image")
                .bio("bio")
                .id(2)
                .password("password")
                .username("username2")
                .build();
        given(userRepo.findUserByUsername(currentUser.getUsername())).willReturn(Optional.of(currentUser));
        given(userRepo.findUserByUsername(userToFollow.getUsername())).willReturn(Optional.of(userToFollow));
        given(userFollowRepo.existsByFollowerAndFollowed(currentUser, userToFollow)).willReturn(false);


        ProfileResponse profileResponse = underTest.followUser(userToFollow.getUsername(), currentUser.getUsername());

        ArgumentCaptor<UserFollow> userFollowArgumentCaptor = ArgumentCaptor.forClass(UserFollow.class);
        verify(userFollowRepo).save(userFollowArgumentCaptor.capture());

        UserFollow capturedUserFollow = userFollowArgumentCaptor.getValue();
        assertThat(capturedUserFollow.getFollower()).isEqualTo(currentUser);
        assertThat(capturedUserFollow.getFollowed()).isEqualTo(userToFollow);
        assertThat(profileResponse).isNotNull();
    }

    @Test
    void FollowUser_TryingToFollowYourself_ShouldThrowCantFollowYourselfOrAlreadyFollowedException() {
        User currentUser = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(currentUser));

        assertThatThrownBy(() -> underTest.followUser(currentUser.getUsername(), currentUser.getUsername()))
                .isInstanceOf(CantFollowYourselfOrAlreadyFollowedException.class);
        verify(userFollowRepo, never()).save(any());
    }

    @Test
    void UnfollowUser_InvokeMethod_ShouldUnfollowUserAndReturnProfile() {
        User currentUser = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        User userToUnfollow = User.builder()
                .email("test2@gmail.com")
                .image("image")
                .bio("bio")
                .id(2)
                .password("password")
                .username("username2")
                .build();
        given(userRepo.findUserByUsername(currentUser.getUsername())).willReturn(Optional.of(currentUser));
        given(userRepo.findUserByUsername(userToUnfollow.getUsername())).willReturn(Optional.of(userToUnfollow));
        UserFollow userFollow = new UserFollow(currentUser, userToUnfollow);
        given(userFollowRepo.findUserFollowByFollowerAndFollowed(currentUser, userToUnfollow)).willReturn(Optional.of(userFollow));


        ProfileResponse profileResponse = underTest.unfollowUser(userToUnfollow.getUsername(), currentUser.getUsername());

        ArgumentCaptor<UserFollow> userFollowArgumentCaptor = ArgumentCaptor.forClass(UserFollow.class);
        verify(userFollowRepo).delete(userFollowArgumentCaptor.capture());

        UserFollow capturedUserFollow = userFollowArgumentCaptor.getValue();
        assertThat(capturedUserFollow.getFollower()).isEqualTo(currentUser);
        assertThat(capturedUserFollow.getFollowed()).isEqualTo(userToUnfollow);
        assertThat(profileResponse).isNotNull();
    }

    @Test
    void UnfollowUser_UserIsNotFollowed_ShouldThrowException() {
        User currentUser = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        User userToUnfollow = User.builder()
                .email("test2@gmail.com")
                .image("image")
                .bio("bio")
                .id(2)
                .password("password")
                .username("username2")
                .build();
        given(userRepo.findUserByUsername(currentUser.getUsername())).willReturn(Optional.of(currentUser));
        given(userRepo.findUserByUsername(userToUnfollow.getUsername())).willReturn(Optional.of(userToUnfollow));
        given(userFollowRepo.findUserFollowByFollowerAndFollowed(currentUser, userToUnfollow)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.unfollowUser(userToUnfollow.getUsername(), currentUser.getUsername()))
                .isInstanceOf(UserNotFoundException.class);
    }
}