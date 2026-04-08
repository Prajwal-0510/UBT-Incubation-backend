package com.ubt.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlumniResponse {
    private Long id;
    private String name;
    private String batch;
    private String degree;
    private String institution;
    private String currentRole;
    private String achievement;
    private String img;
    private String category;
    private String linkedIn;
    private LocalDateTime createdAt;
}
