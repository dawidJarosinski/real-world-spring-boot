package org.example.realworldspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepository;
    private final JwtService jwtService;

    public UserResponse getActualUser(String email) {
        User user = userRepository.findUserByEmail(email).get();

        return UserResponse.builder()
                .user(
                        UserResponse.User
                                .builder()
                                .token(jwtService.generateToken(user))
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .build())
                .build();
    }

}
