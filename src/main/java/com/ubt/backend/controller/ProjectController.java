package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.ProjectRequest;
import com.ubt.backend.dto.ProjectResponse;
import com.ubt.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * GET /api/projects
     * Returns all active projects (public)
     * Optional query param: ?level=BE/BTech
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAll(
            @RequestParam(required = false) String level) {

        List<ProjectResponse> projects = (level != null && !level.isBlank())
                ? projectService.getProjectsByLevel(level)
                : projectService.getAllProjects();

        return ResponseEntity.ok(
                ApiResponse.ok(projects, "Projects fetched successfully"));
    }

    /**
     * POST /api/projects
     * Create a new project (ADMIN only)
     * Body: { "title": "...", "level": "BE/BTech", "domain": "...", ... }
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> addProject(
            @Valid @RequestBody ProjectRequest request) {

        ProjectResponse created = projectService.addProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Project added successfully"));
    }

    /**
     * DELETE /api/projects/{id}
     * Soft-delete a project (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.ok("Project deleted successfully"));
    }
}
