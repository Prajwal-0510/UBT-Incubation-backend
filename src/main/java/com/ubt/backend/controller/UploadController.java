package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * UploadController — handles all image uploads to Cloudinary.
 *
 * Endpoints:
 *   POST /upload/image  — used by ALL frontend components (gallery, projects, alumni)
 *   POST /upload        — legacy alias kept for backward compatibility
 *
 * Both endpoints are marked permitAll() in SecurityConfig so they work
 * with OR without a JWT token. The frontend still sends the JWT when
 * the admin is logged in (handled in api.js).
 *
 * Returns: ApiResponse<String> where data = Cloudinary secure_url
 */
@RestController
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    private static final long   MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB
    private static final Set<String> ALLOWED_TYPES  = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp",
            "image/gif",  "image/bmp", "image/svg+xml"
    );

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * POST /upload/image
     * Primary upload endpoint — referenced by api.js uploadImage()
     */
    @PostMapping(
            value    = "/upload/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        return doUpload(file);
    }

    /**
     * POST /upload
     * Legacy alias — kept so any old references still work
     */
    @PostMapping(
            value    = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<String>> uploadLegacy(
            @RequestParam("file") MultipartFile file) {
        return doUpload(file);
    }

    // ── Shared upload logic ───────────────────────────────────────────────────
    private ResponseEntity<ApiResponse<String>> doUpload(MultipartFile file) {

        // 1. Null / empty check
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No file provided. Please select an image."));
        }

        // 2. Content-type validation
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(
                            "Invalid file type: " + contentType +
                                    ". Allowed: JPG, PNG, WEBP, GIF, BMP, SVG."
                    ));
        }

        // 3. File size validation (belt-and-suspenders; multipart config also limits)
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File too large. Maximum allowed size is 10 MB."));
        }

        // 4. Upload to Cloudinary
        try {
            String cloudinaryUrl = cloudinaryService.uploadImage(file);
            log.info("Image uploaded successfully: {}", cloudinaryUrl);
            return ResponseEntity.ok(
                    ApiResponse.ok(cloudinaryUrl, "Image uploaded successfully.")
            );
        } catch (Exception e) {
            log.error("Cloudinary upload failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(
                            "Image upload failed: " + e.getMessage() +
                                    ". Please try again or contact support."
                    ));
        }
    }
}
