package com.ubt.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ubt.backend.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Image upload endpoint — admin uploads image, gets back a public URL.
 * Stores image in Cloudinary so it persists permanently.
 * POST /api/upload/image  (multipart/form-data, field name: file)
 */
@RestController
@RequestMapping("/upload")
public class ImageUploadController {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;
    @Value("${cloudinary.api-key:}")
    private String apiKey;
    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    @PostMapping("/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("No file provided"));
            }

            // If Cloudinary is configured, upload there
            if (!cloudName.isBlank() && !apiKey.isBlank() && !apiSecret.isBlank()) {
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key",    apiKey,
                        "api_secret", apiSecret,
                        "secure",     true
                ));
                Map<?, ?> result = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder",          "ubt-technology",
                                "resource_type",   "image",
                                "quality",         "auto",
                                "fetch_format",    "auto"
                        )
                );
                String url = (String) result.get("secure_url");
                return ResponseEntity.ok(ApiResponse.ok(url, "Image uploaded to Cloudinary"));
            }

            // Fallback: return base64 data URL (for local dev without Cloudinary)
            String base64 = java.util.Base64.getEncoder().encodeToString(file.getBytes());
            String dataUrl = "data:" + file.getContentType() + ";base64," + base64;
            return ResponseEntity.ok(ApiResponse.ok(dataUrl, "Image uploaded as base64"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Upload failed: " + e.getMessage()));
        }
    }
}
