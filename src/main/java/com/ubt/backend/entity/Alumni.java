package com.ubt.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "alumni")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 10)
    private String batch;

    @Column(length = 200)
    private String degree;

    @Column(length = 200)
    private String institution;

    @Column(name = "current_role", length = 300)
    private String currentRole;

    @Column(columnDefinition = "TEXT")
    private String achievement;

    @Column(name = "img_url", columnDefinition = "MEDIUMTEXT")
    private String img;   // URL or base64

    @Column(length = 30)
    @Builder.Default
    private String category = "industry"; // faculty | industry | research

    @Column(name = "linked_in", length = 500)
    private String linkedIn;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
