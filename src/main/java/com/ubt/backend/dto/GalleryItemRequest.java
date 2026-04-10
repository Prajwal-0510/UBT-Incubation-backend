package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * GalleryItemRequest DTO — FIXED
 *
 * KEY FIX: Removed @Size(max = 255) from the img field.
 * Cloudinary URLs can easily exceed 255 characters, especially
 * when they include transformation parameters or folder paths.
 * The DB column is already MEDIUMTEXT so no length limit is needed here.
 */
@Data
public class GalleryItemRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Category is required")
    private String category; // 'college-visit' or 'student-project'

    @NotBlank(message = "Image URL is required")
    // @Size(max = 255) ← REMOVED: Cloudinary URLs exceed 255 chars
    private String img;

    private LocalDate date;
}
