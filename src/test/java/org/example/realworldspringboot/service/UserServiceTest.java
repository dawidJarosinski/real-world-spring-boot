package org.example.realworldspringboot.service;

import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.config.exceptions.UsernameOrEmailIsAlreadyTakenException;
import org.example.realworldspringboot.dto.request.UserRequest;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;
    @Mock
    private UserRepo userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, jwtService, passwordEncoder);
    }

    @Test
    void GetActualUser_InvokeMethod_ShouldReturnUserResponse() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        String token = "123123123";
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(jwtService.generateToken(user)).willReturn(token);

        UserResponse userResponse = underTest.getActualUser("username");

        assertThat(userResponse.user().username()).isEqualTo(user.getUsername());
        assertThat(userResponse.user().token()).isEqualTo(token);
    }

    @Test
    void GetActualUser_UserNotFound_ShouldThrowUserNotFoundException() {
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getActualUser(anyString()))
                .hasMessage("User not found!")
                .isInstanceOf(UserNotFoundException.class);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void UpdateUser_UpdateUser_ShouldReturnCorrectUserResponseAndSaveInRepository() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        String token = "123123123";
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(jwtService.generateToken(user)).willReturn(token);
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("updatedPassword");
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        UserRequest userRequest = new UserRequest(new UserRequest.User("updatedEmail", "updatedPassword", "updatedUsername", "updatedImage", "updatedBio"));


        UserResponse userResponse = underTest.updateUser(userRequest, "username");


        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User actualUser = userArgumentCaptor.getValue();
        assertThat(actualUser.getUsername()).isEqualTo(userResponse.user().username());
        assertThat(actualUser.getPassword()).isEqualTo("updatedPassword");
        assertThat(actualUser.getEmail()).isEqualTo(userResponse.user().email());
        assertThat(actualUser.getBio()).isEqualTo(userResponse.user().bio());
        assertThat(actualUser.getImage()).isEqualTo(userResponse.user().image());
    }

    @Test
    void UpdateUser_UpdateUserWithTakenUsername_ShouldThrowUsernameOrEmailIsAlreadyTakenException() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        given(userRepository.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(userRepository.existsByUsername("updatedUsername")).willReturn(true);

        UserRequest userRequest = new UserRequest(new UserRequest.User(null, null, "updatedUsername", null, null));


        verify(userRepository, never()).save(any());
        assertThatThrownBy(() -> underTest.updateUser(userRequest, "username"))
                .isInstanceOf(UsernameOrEmailIsAlreadyTakenException.class)
                .hasMessage("Username or email is already taken!");
    }
}