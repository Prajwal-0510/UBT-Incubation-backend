package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private ContactInquiryRepository contactInquiryRepository;

    @Autowired
    private TestimonialRepository testimonialRepository;

    /**
     * GET /api/admin/stats
     * Returns dashboard statistics (ADMIN only)
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalGalleryItems",    galleryRepository.findByActiveTrue().size());
        stats.put("totalProjects",        projectRepository.findByActiveTrue().size());
        stats.put("totalUpdates",         updateRepository.findAllActiveOrderByPinnedAndDate().size());
        stats.put("totalInquiries",       contactInquiryRepository.count());
        stats.put("newInquiries",         contactInquiryRepository.findByStatusOrderBySubmittedAtDesc("NEW").size());
        stats.put("totalTestimonials",    testimonialRepository.findByActiveTrueOrderByCreatedAtDesc().size());

        return ResponseEntity.ok(ApiResponse.ok(stats, "Dashboard stats fetched"));
    }
}
