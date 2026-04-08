package com.ubt.backend.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private String name;
    private String role;
    private String avatar;
    private String avatarBg;
    private String avatarColor;
    private Integer rating;
    private String date;
    private String text;
    private String service;
    private Boolean verified;
}