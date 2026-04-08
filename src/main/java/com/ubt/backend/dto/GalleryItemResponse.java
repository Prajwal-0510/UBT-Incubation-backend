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
public class GalleryItemResponse {
    private Long id;
    private String title;
    private String category;
    private String img;
    private LocalDate date;
    private LocalDateTime createdAt;
}
