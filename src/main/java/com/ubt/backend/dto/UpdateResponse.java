package com.ubt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private Long id;
    private String type;
    private String title;
    private String content;
    private LocalDate date;
    private Boolean pinned;
    private LocalDateTime createdAt;
}
