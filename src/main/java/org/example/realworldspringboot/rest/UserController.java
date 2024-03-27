package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.UserResponse;
import org.example.realworldspringboot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

//    @GetMapping("/user")
//    public ResponseEntity<UserResponse> getActualUser(Authentication authenticaion) {
//        return ResponseEntity.ok(userService.getActualUser(authenticaion.getName()));
//    }

    @GetMapping("/hello")
    public String getActualUser(Principal principal) {
        return "hello" + principal.getName();
    }
}
