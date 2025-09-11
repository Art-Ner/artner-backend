package kr.artner.domain.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @PostMapping
    public ResponseEntity<?> createProject() { return ResponseEntity.ok().build(); }

    @PatchMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId) { return ResponseEntity.ok().build(); }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) { return ResponseEntity.ok().build(); }

    @GetMapping
    public ResponseEntity<?> getProjects() { return ResponseEntity.ok().build(); }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectDetail(@PathVariable Long projectId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{projectId}/collab-requests")
    public ResponseEntity<?> requestCollab(@PathVariable Long projectId) { return ResponseEntity.ok().build(); }

    @PatchMapping("/{projectId}/collab-requests/{requestId}/accept")
    public ResponseEntity<?> acceptCollab(@PathVariable Long projectId, @PathVariable Long requestId) { return ResponseEntity.ok().build(); }

    @PatchMapping("/{projectId}/collab-requests/{requestId}/reject")
    public ResponseEntity<?> rejectCollab(@PathVariable Long projectId, @PathVariable Long requestId) { return ResponseEntity.ok().build(); }
}
