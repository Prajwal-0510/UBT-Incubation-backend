package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.service.ImageService; // ✅ FIXED
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestParam("file") MultipartFile file) {

        String url = imageService.uploadImage(file);
        return ResponseEntity.ok(ApiResponse.ok(url, "Image uploaded"));
    }
}