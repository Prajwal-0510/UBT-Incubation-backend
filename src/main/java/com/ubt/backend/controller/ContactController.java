package com.ubt.backend.controller;

import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.ContactInquiryResponse;
import com.ubt.backend.dto.ContactRequest;
import com.ubt.backend.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * POST /api/contact
     * Submit a contact/inquiry form (public)
     * Body: { "name": "...", "email": "...", "phone": "...", "service": "...", "message": "..." }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ContactInquiryResponse>> submit(
            @Valid @RequestBody ContactRequest request) {

        ContactInquiryResponse response = contactService.submitInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Your inquiry has been submitted. We will contact you within 24 hours."));
    }

    /**
     * GET /api/contact/inquiries
     * Get all inquiries (ADMIN only)
     * Optional: ?status=NEW
     */
    @GetMapping("/inquiries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ContactInquiryResponse>>> getAll(
            @RequestParam(required = false) String status) {

        List<ContactInquiryResponse> inquiries = (status != null && !status.isBlank())
                ? contactService.getInquiriesByStatus(status)
                : contactService.getAllInquiries();

        return ResponseEntity.ok(
                ApiResponse.ok(inquiries, "Inquiries fetched successfully"));
    }

    /**
     * PATCH /api/contact/inquiries/{id}/status
     * Update inquiry status (ADMIN only)
     * Body: { "status": "IN_PROGRESS" }
     */
    @PatchMapping("/inquiries/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContactInquiryResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Status field is required"));
        }

        ContactInquiryResponse updated = contactService.updateStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.ok(updated, "Inquiry status updated successfully"));
    }

    /**
     * DELETE /api/contact/inquiries/{id}
     * Delete an inquiry (ADMIN only)
     */
    @DeleteMapping("/inquiries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(@PathVariable Long id) {
        contactService.deleteInquiry(id);
        return ResponseEntity.ok(ApiResponse.ok("Inquiry deleted successfully"));
    }
}
