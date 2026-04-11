package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.UpdateRequest;
import com.ubt.backend.dto.UpdateResponse;
import com.ubt.backend.service.UpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/updates")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UpdateResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(updateService.getAllUpdates(), "Fetched"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UpdateResponse>> add(@Valid @RequestBody UpdateRequest body) {
        return ResponseEntity.ok(ApiResponse.ok(updateService.addUpdate(body), "Added"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        updateService.deleteUpdate(id);
        return ResponseEntity.ok(ApiResponse.ok("Deleted"));
    }

    @PatchMapping("/{id}/pin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UpdateResponse>> togglePin(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(updateService.togglePin(id), "Toggled"));
    }
}