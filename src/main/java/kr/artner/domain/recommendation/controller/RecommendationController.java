package kr.artner.domain.recommendation.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @PostMapping("/batch/generate")
    public ResponseEntity<?> generateRecommendations() { return ResponseEntity.ok().build(); }

    @GetMapping("/projects")
    public ResponseEntity<?> getRecommendedProjects() { return ResponseEntity.ok().build(); }
}
