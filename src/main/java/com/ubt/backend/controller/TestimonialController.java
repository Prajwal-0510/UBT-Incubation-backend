package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.TestimonialRequest;
import com.ubt.backend.dto.TestimonialResponse;
import com.ubt.backend.service.TestimonialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
public class TestimonialController {

    @Autowired
    private TestimonialService testimonialService;

    /**
     * GET /api/testimonials
     * Returns all active testimonials (public)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TestimonialResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(testimonialService.getAllTestimonials(),
                        "Testimonials fetched successfully"));
    }

    /**
     * POST /api/testimonials
     * Add a testimonial (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestimonialResponse>> add(
            @Valid @RequestBody TestimonialRequest request) {

        TestimonialResponse created = testimonialService.addTestimonial(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Testimonial added successfully"));
    }

    /**
     * DELETE /api/testimonials/{id}
     * Soft-delete a testimonial (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.ok(ApiResponse.ok("Testimonial deleted successfully"));
    }
}
