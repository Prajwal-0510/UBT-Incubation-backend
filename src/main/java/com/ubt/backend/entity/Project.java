package com.ubt.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(length = 150)
    private String domain;

    // 'PhD R&D', 'ME/MTech', 'BE/BTech', 'MBA Projects'
    @Column(nullable = false, length = 50)
    private String level;

    @Column(name = "img_url", length = 1000)
    private String img;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Stored as comma-separated, converted via helper
    @Column(name = "tech_stack", length = 500)
    private String techStack;

    @Column(length = 50)
    private String duration;

    @Column(name = "project_date", length = 10)
    private String date; // e.g. "2024-03"

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper to get tech as list
    @Transient
    public List<String> getTechList() {
        if (techStack == null || techStack.isBlank()) return List.of();
        return List.of(techStack.split(","));
    }
}
