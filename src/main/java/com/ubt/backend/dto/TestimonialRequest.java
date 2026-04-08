package com.ubt.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestimonialRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String role;

    @NotBlank(message = "Text is required")
    private String text;

    private Integer rating = 5;

    private String avatar;
}
