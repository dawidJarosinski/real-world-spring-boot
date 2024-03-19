package org.example.realworldspringboot.service;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.UserAlreadyExistsException;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.dto.request.AuthenticationRequest;
import org.example.realworldspringboot.dto.request.RegistrationRequest;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepository;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public UserResponse register(RegistrationRequest request) {

        if(userRepository.existsByEmail(request.user().email()) || userRepository.existsByUsername(request.user().username())) {
            throw new UserAlreadyExistsException();
        }
        User user = User
                .builder()
                .username(request.user().username())
                .email(request.user().email())
                .password(passwordEncoder.encode(request.user().password()))
                .build();

        userRepository.save(user);

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

    public UserResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.user().email(),
                        request.user().password()));

        User user = userRepository.findUserByEmail(request.user().email()).orElseThrow(UserNotFoundException::new);

        return UserResponse.builder()
                .user(
                        UserResponse.User
                                .builder()
                                .token(jwtService.generateToken(user))
                                .email(user.getEmail())
                                .bio(user.getBio())
                                .image(user.getImage())
                                .username(user.getUsername())
                                .build())
                .build();
    }
}
