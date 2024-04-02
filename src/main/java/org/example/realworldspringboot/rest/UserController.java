package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.request.UserRequest;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getActualUser(Principal principal) {
        return ResponseEntity.ok(userService.getActualUser(principal.getName()));
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, Principal principal) {
        return ResponseEntity.ok(userService.updateUser(userRequest, principal.getName()));
    }
}
