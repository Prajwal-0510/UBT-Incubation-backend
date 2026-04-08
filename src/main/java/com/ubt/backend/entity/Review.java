package com.ubt.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 200)
    private String role;

    @Column(length = 10)
    private String avatar;   // 2-letter initials e.g. "PS"

    @Column(length = 20)
    private String avatarBg;

    @Column(length = 20)
    private String avatarColor;

    @Min(1) @Max(5)
    @Column(nullable = false)
    @Builder.Default
    private Integer rating = 5;

    @Column(length = 30)
    private String date;    // e.g. "March 2025"

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(length = 100)
    private String service;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean verified = true;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}