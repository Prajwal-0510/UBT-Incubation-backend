package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRequest {

    @NotBlank(message = "Type is required")
    private String type; // 'announcement', 'achievement', 'event', 'news'

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDate date;

    private Boolean pinned = false;
}
