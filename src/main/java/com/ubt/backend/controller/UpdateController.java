package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/updates")
public class UpdateController {

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUpdates() {
        return ResponseEntity.ok(ApiResponse.ok(List.of(), "Updates fetched"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addUpdate(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ApiResponse.ok(body, "Update added"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteUpdate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Deleted"));
    }

    @PatchMapping("/{id}/pin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> togglePin(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Toggled"));
    }
}