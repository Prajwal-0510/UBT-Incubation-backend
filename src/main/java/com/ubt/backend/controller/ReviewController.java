package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.ReviewResponse;
import com.ubt.backend.entity.Review;
import com.ubt.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // GET /api/reviews
    // Returns all active reviews with aggregate stats
    // Response: { success, message, data: { reviews, total, average, distribution } }
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReviews(
            @RequestParam(required = false) Integer rating) {

        List<Review> all = reviewRepository.findByActiveTrueOrderByCreatedAtDesc();
        List<Review> filtered = rating != null
                ? all.stream().filter(r -> r.getRating().equals(rating)).collect(Collectors.toList())
                : all;

        // Compute average
        double avg = all.stream().mapToInt(Review::getRating).average().orElse(0.0);

        // Compute distribution
        Map<Integer, Long> dist = all.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        List<ReviewResponse> reviews = filtered.stream().map(r ->
                ReviewResponse.builder()
                        .id(r.getId()).name(r.getName()).role(r.getRole())
                        .avatar(r.getAvatar()).avatarBg(r.getAvatarBg()).avatarColor(r.getAvatarColor())
                        .rating(r.getRating()).date(r.getDate()).text(r.getText())
                        .service(r.getService()).verified(r.getVerified())
                        .build()
        ).collect(Collectors.toList());

        Map<String, Object> data = Map.of(
                "reviews",      reviews,
                "total",        all.size(),
                "average",      Math.round(avg * 10.0) / 10.0,
                "distribution", dist
        );

        return ResponseEntity.ok(ApiResponse.ok(data, "Reviews fetched successfully"));
    }
}