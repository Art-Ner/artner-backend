package kr.artner.domain.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin() { return ResponseEntity.ok().build(); }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin() { return ResponseEntity.ok().build(); }

}
