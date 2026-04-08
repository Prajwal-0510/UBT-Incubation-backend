package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.GalleryItemRequest;
import com.ubt.backend.dto.GalleryItemResponse;
import com.ubt.backend.service.GalleryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    /**
     * GET /api/gallery
     * Returns all gallery items (public)
     * Optional query param: ?category=college-visit OR ?category=student-project
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GalleryItemResponse>>> getAll(
            @RequestParam(required = false) String category) {

        List<GalleryItemResponse> items = (category != null && !category.isBlank())
                ? galleryService.getItemsByCategory(category)
                : galleryService.getAllItems();

        return ResponseEntity.ok(
                ApiResponse.ok(items, "Gallery items fetched successfully"));
    }

    /**
     * POST /api/gallery
     * Add a new gallery item (ADMIN only)
     * Body: { "title": "...", "category": "college-visit", "img": "https://..." }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GalleryItemResponse>> addItem(
            @Valid @RequestBody GalleryItemRequest request) {

        GalleryItemResponse created = galleryService.addItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Gallery item added successfully"));
    }

    /**
     * DELETE /api/gallery/{id}
     * Soft-delete a gallery item (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        galleryService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.ok("Gallery item deleted successfully"));
    }
}
