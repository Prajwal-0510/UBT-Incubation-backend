package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.UpdateRequest;
import com.ubt.backend.dto.UpdateResponse;
import com.ubt.backend.service.UpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/updates")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    /**
     * GET /api/updates
     * Returns all active updates, pinned first (public)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UpdateResponse>>> getAll() {
        List<UpdateResponse> updates = updateService.getAllUpdates();
        return ResponseEntity.ok(
                ApiResponse.ok(updates, "Updates fetched successfully"));
    }

    /**
     * POST /api/updates
     * Create a new update/announcement (ADMIN only)
     * Body: { "type": "announcement", "title": "...", "content": "..." }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UpdateResponse>> addUpdate(
            @Valid @RequestBody UpdateRequest request) {

        UpdateResponse created = updateService.addUpdate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Update posted successfully"));
    }

    /**
     * DELETE /api/updates/{id}
     * Soft-delete an update (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUpdate(@PathVariable Long id) {
        updateService.deleteUpdate(id);
        return ResponseEntity.ok(ApiResponse.ok("Update deleted successfully"));
    }

    /**
     * PATCH /api/updates/{id}/pin
     * Toggle pinned status of an update (ADMIN only)
     */
    @PatchMapping("/{id}/pin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UpdateResponse>> togglePin(@PathVariable Long id) {
        UpdateResponse updated = updateService.togglePin(id);
        return ResponseEntity.ok(
                ApiResponse.ok(updated, "Pin status toggled successfully"));
    }
}
