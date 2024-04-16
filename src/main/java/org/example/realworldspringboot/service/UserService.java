package org.example.realworldspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.config.exceptions.UsernameOrEmailIsAlreadyTakenException;
import org.example.realworldspringboot.dto.request.UserRequest;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getActualUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

        return UserResponse.builder()
                .user(
                        UserResponse.User
                                .builder()
                                .token(jwtService.generateToken(user))
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .bio(user.getBio())
                                .image(user.getImage())
                                .build())
                .build();
    }
    @Transactional
    public UserResponse updateUser(UserRequest userRequest, String actualUserUsername) {
        User user  = userRepository.findUserByUsername(actualUserUsername).orElseThrow(UserNotFoundException::new);
        if(userRequest.user().email() != null) {
            if(userRepository.existsByEmail(userRequest.user().email())) {
                throw new UsernameOrEmailIsAlreadyTakenException();
            }
            user.setEmail(userRequest.user().email());
        }
        if(userRequest.user().password() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.user().password()));
        }
        if(userRequest.user().username() != null) {
            if(userRepository.existsByUsername(userRequest.user().username())) {
                throw new UsernameOrEmailIsAlreadyTakenException();
            }
            user.setUsername(userRequest.user().username());
        }
        if(userRequest.user().bio() != null) {
            user.setBio(userRequest.user().bio());
        }
        if(userRequest.user().image() != null) {
            user.setImage(userRequest.user().image());
        }

        return UserResponse.builder()
                .user(
                        UserResponse.User
                                .builder()
                                .token(jwtService.generateToken(user))
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .bio(user.getBio())
                                .image(user.getImage())
                                .build())
                .build();
    }
}
