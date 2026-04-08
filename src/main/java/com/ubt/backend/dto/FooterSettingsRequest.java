package com.ubt.backend.dto;

import lombok.Data;

@Data
public class FooterSettingsRequest {
    private String email;
    private String phone;
    private String address;
    private String workingHours;
    private String whatsapp;
    private String linkedinUrl;
    private String instagramUrl;
}
