package com.ubt.backend.service;

import com.ubt.backend.dto.ContactInquiryResponse;
import com.ubt.backend.dto.ContactRequest;

import java.util.List;

public interface ContactService {

    ContactInquiryResponse submitInquiry(ContactRequest request);

    List<ContactInquiryResponse> getAllInquiries();

    List<ContactInquiryResponse> getInquiriesByStatus(String status);

    ContactInquiryResponse updateStatus(Long id, String status);

    void deleteInquiry(Long id);
}
