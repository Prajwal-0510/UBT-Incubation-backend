package com.ubt.backend.service.impl;

import com.ubt.backend.dto.TestimonialRequest;
import com.ubt.backend.dto.TestimonialResponse;
import com.ubt.backend.entity.Testimonial;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.TestimonialRepository;
import com.ubt.backend.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestimonialServiceImpl implements TestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TestimonialResponse> getAllTestimonials() {
        return testimonialRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TestimonialResponse addTestimonial(TestimonialRequest request) {
        Testimonial t = Testimonial.builder()
                .name(request.getName())
                .role(request.getRole())
                .text(request.getText())
                .rating(request.getRating() != null ? request.getRating() : 5)
                .avatar(request.getAvatar())
                .active(true)
                .build();
        return toResponse(testimonialRepository.save(t));
    }

    @Override
    public void deleteTestimonial(Long id) {
        Testimonial t = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial", id));
        t.setActive(false);
        testimonialRepository.save(t);
    }

    private TestimonialResponse toResponse(Testimonial t) {
        return TestimonialResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .role(t.getRole())
                .text(t.getText())
                .rating(t.getRating())
                .avatar(t.getAvatar())
                .build();
    }
}
