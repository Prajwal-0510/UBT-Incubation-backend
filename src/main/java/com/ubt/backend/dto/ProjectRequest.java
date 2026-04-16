package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * ProjectRequest — received from frontend on POST /projects.
 *
 * Frontend sends:
 *   { title, domain, level, img, desc, tech: [...], duration, date }
 *
 * "desc" is used here (not "description") so the JSON field name matches
 * exactly what AdminContext.jsx sends via addProject().
 */
@Data
public class ProjectRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String domain;

    @NotBlank(message = "Level is required")
    private String level; // 'PhD R&D' | 'ME/MTech' | 'BE/BTech' | 'MBA Projects'

    private String img; // Cloudinary URL (uploaded before this request is made)

    private String desc; // project description — matches frontend form.desc key

    private List<String> tech; // array of tech tags e.g. ["React","Python"]

    private String duration;

    private String date; // "YYYY-MM" e.g. "2024-03"
}
