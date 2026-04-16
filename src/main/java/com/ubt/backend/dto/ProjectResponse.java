package com.ubt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ProjectResponse — sent to frontend.
 *
 * FIELD NAMING CONTRACT (must match what AdminContext.jsx / OnlineProjectsSection.jsx reads):
 *   id, title, domain, level, img, desc, tech, duration, date, createdAt
 *
 * Note: backend entity uses `imgUrl` and `description` internally;
 * those are mapped to `img` and `desc` here in ProjectServiceImpl.toResponse().
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Long   id;
    private String title;
    private String domain;
    private String level;
    private String img;        // mapped from entity.imgUrl
    private String desc;       // mapped from entity.description
    private List<String> tech; // converted from comma-separated entity.techStack
    private String duration;
    private String date;
    private LocalDateTime createdAt;
}
