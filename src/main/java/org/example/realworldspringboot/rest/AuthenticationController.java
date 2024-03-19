package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.request.AuthenticationRequest;
import org.example.realworldspringboot.dto.request.RegistrationRequest;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }
}
