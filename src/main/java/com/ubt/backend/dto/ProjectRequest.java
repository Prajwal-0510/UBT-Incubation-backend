package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProjectRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String domain;

    @NotBlank(message = "Level is required")
    private String level; // 'PhD R&D', 'ME/MTech', 'BE/BTech', 'MBA Projects'

    private String img;

    private String description;

    private List<String> tech; // sent as array from frontend

    private String duration;

    private String date; // "2024-03"
}
