package com.ubt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactInquiryResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String service;
    private String message;
    private String status;
    private LocalDateTime submittedAt;
}
