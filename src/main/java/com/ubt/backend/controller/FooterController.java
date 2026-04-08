package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.FooterSettingsRequest;
import com.ubt.backend.entity.FooterSettings;
import com.ubt.backend.repository.FooterSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/footer")
public class FooterController {

    @Autowired
    private FooterSettingsRepository footerRepository;

    /**
     * GET /api/footer — PUBLIC, no auth needed
     * Returns the single footer settings record
     */
    @GetMapping
    public ResponseEntity<ApiResponse<FooterSettings>> get() {
        FooterSettings settings = footerRepository.findById(1L)
                .orElse(getDefaultSettings());
        return ResponseEntity.ok(ApiResponse.ok(settings, "Footer settings fetched"));
    }

    /**
     * PUT /api/footer — ADMIN only
     * Update footer settings (contact, address, hours etc.)
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FooterSettings>> update(@RequestBody FooterSettingsRequest req) {
        FooterSettings settings = footerRepository.findById(1L)
                .orElse(getDefaultSettings());
        settings.setId(1L);

        if (req.getEmail()        != null) settings.setEmail(req.getEmail());
        if (req.getPhone()        != null) settings.setPhone(req.getPhone());
        if (req.getAddress()      != null) settings.setAddress(req.getAddress());
        if (req.getWorkingHours() != null) settings.setWorkingHours(req.getWorkingHours());
        if (req.getWhatsapp()     != null) settings.setWhatsapp(req.getWhatsapp());
        if (req.getLinkedinUrl()  != null) settings.setLinkedinUrl(req.getLinkedinUrl());
        if (req.getInstagramUrl() != null) settings.setInstagramUrl(req.getInstagramUrl());

        FooterSettings saved = footerRepository.save(settings);
        return ResponseEntity.ok(ApiResponse.ok(saved, "Footer updated successfully"));
    }

    private FooterSettings getDefaultSettings() {
        return FooterSettings.builder()
                .id(1L)
                .email("info@ubtorg.com")
                .phone("+91 9370272741")
                .address("Office No. 709, Seventh Floor, Landmark Center, Pune-Satara Road, Parvati, Pune")
                .workingHours("Mon–Sat: 9:00 AM – 7:00 PM")
                .whatsapp("+919370272741")
                .build();
    }
}
