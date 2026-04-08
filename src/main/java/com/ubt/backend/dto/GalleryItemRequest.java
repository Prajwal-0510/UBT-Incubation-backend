package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GalleryItemRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Category is required")
    private String category; // 'college-visit' or 'student-project'

    @NotBlank(message = "Image URL is required")
    private String img;

    private LocalDate date;
}
