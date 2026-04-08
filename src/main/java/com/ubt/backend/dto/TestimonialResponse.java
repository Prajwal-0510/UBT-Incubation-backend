package com.ubt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialResponse {
    private Long id;
    private String name;
    private String role;
    private String text;
    private Integer rating;
    private String avatar;
}
