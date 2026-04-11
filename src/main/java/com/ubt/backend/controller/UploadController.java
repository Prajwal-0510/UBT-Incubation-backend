package com.ubt.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ubt.backend.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    // Handles both /upload AND /upload/image (frontend calls both)
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
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
            String url = (String) result.get("secure_url");
            return ResponseEntity.ok(ApiResponse.ok(url, "Uploaded successfully"));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }
}