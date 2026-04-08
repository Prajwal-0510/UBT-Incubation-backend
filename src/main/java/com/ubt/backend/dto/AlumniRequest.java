package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlumniRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String batch;
    private String degree;
    private String institution;
    private String currentRole;
    private String achievement;
    private String img;
    private String category;  // faculty | industry | research
    private String linkedIn;
}
