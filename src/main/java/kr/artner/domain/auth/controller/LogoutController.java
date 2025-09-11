package kr.artner.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {
    @PostMapping
    public ResponseEntity<?> logout() {
        // TODO: Implement logout logic
        return ResponseEntity.ok().build();
    }
}