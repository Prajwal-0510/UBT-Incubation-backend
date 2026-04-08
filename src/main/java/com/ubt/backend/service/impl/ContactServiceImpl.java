package com.ubt.backend.service.impl;

import com.ubt.backend.dto.ContactInquiryResponse;
import com.ubt.backend.dto.ContactRequest;
import com.ubt.backend.entity.ContactInquiry;
import com.ubt.backend.exception.BadRequestException;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.ContactInquiryRepository;
import com.ubt.backend.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private static final List<String> VALID_STATUSES =
            List.of("NEW", "IN_PROGRESS", "RESOLVED", "CLOSED");

    @Autowired
    private ContactInquiryRepository contactRepository;

    // Mail sender may not be configured — use Optional injection via try-catch
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.to:info@ubtorg.com}")
    private String mailTo;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Override
    public ContactInquiryResponse submitInquiry(ContactRequest request) {
        ContactInquiry inquiry = ContactInquiry.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .service(request.getService())
                .message(request.getMessage())
                .status("NEW")
                .build();

        ContactInquiry saved = contactRepository.save(inquiry);

        // Attempt to send notification email (non-blocking — errors are logged, not thrown)
        sendNotificationEmail(saved);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactInquiryResponse> getAllInquiries() {
        return contactRepository.findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactInquiryResponse> getInquiriesByStatus(String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new BadRequestException("Invalid status. Must be one of: " + VALID_STATUSES);
        }
        return contactRepository.findByStatusOrderBySubmittedAtDesc(status.toUpperCase())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContactInquiryResponse updateStatus(Long id, String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new BadRequestException("Invalid status. Must be one of: " + VALID_STATUSES);
        }
        ContactInquiry inquiry = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactInquiry", id));
        inquiry.setStatus(status.toUpperCase());
        return toResponse(contactRepository.save(inquiry));
    }

    @Override
    public void deleteInquiry(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("ContactInquiry", id);
        }
        contactRepository.deleteById(id);
    }

    private void sendNotificationEmail(ContactInquiry inquiry) {
        if (mailSender == null || mailFrom == null || mailFrom.isBlank()) {
            log.info("Mail sender not configured — skipping email notification for inquiry id={}", inquiry.getId());
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(mailTo);
            message.setSubject("New Inquiry from UBT Website — " + inquiry.getName());
            message.setText(buildEmailBody(inquiry));
            mailSender.send(message);
            log.info("Notification email sent for inquiry id={}", inquiry.getId());
        } catch (Exception e) {
            log.warn("Failed to send notification email for inquiry id={}: {}", inquiry.getId(), e.getMessage());
        }
    }

    private String buildEmailBody(ContactInquiry i) {
        return String.format("""
                New inquiry received on UBT Technology website.
                
                Name    : %s
                Email   : %s
                Phone   : %s
                Service : %s
                
                Message:
                %s
                
                -- UBT Technology Auto-Notification
                """,
                i.getName(), i.getEmail(),
                i.getPhone() != null ? i.getPhone() : "N/A",
                i.getService() != null ? i.getService() : "N/A",
                i.getMessage() != null ? i.getMessage() : "");
    }

    private ContactInquiryResponse toResponse(ContactInquiry i) {
        return ContactInquiryResponse.builder()
                .id(i.getId())
                .name(i.getName())
                .email(i.getEmail())
                .phone(i.getPhone())
                .service(i.getService())
                .message(i.getMessage())
                .status(i.getStatus())
                .submittedAt(i.getSubmittedAt())
                .build();
    }
}
