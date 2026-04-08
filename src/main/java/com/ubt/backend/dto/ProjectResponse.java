package com.ubt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Long id;
    private String title;
    private String domain;
    private String level;
    private String img;
    private String desc;
    private List<String> tech;
    private String duration;
    private String date;
    private LocalDateTime createdAt;
}
