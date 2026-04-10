package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/updates")
public class UpdateController {

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUpdates() {

        // 🔥 TEMP DATA (so frontend stops breaking)
        List<Map<String, String>> updates = List.of(
                Map.of("title", "UBT Platform Live 🚀", "content", "Our platform is now live!"),
                Map.of("title", "Gallery Feature Added", "content", "You can now upload images.")
        );

        return ResponseEntity.ok(
                ApiResponse.ok(updates, "Updates fetched successfully")
        );
    }
}