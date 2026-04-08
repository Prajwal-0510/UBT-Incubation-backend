package com.ubt.backend.service;

import com.ubt.backend.dto.TestimonialRequest;
import com.ubt.backend.dto.TestimonialResponse;

import java.util.List;

public interface TestimonialService {

    List<TestimonialResponse> getAllTestimonials();

    TestimonialResponse addTestimonial(TestimonialRequest request);

    void deleteTestimonial(Long id);
}
