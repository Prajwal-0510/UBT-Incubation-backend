package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping({"/upload", "/upload/image"})
    public ResponseEntity<ApiResponse<String>> upload(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No file provided"));

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Only image files are allowed"));

        try {
            String url = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(ApiResponse.ok(url, "Uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }
}