package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(profileService.findByUsername(username, principal.getName()));
    }

    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileResponse> followUser(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(profileService.followUser(username, principal.getName()));
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollowUser(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(profileService.unfollowUser(username, principal.getName()));
    }
}
