package com.ubt.backend.controller;

import com.ubt.backend.dto.AlumniRequest;
import com.ubt.backend.dto.AlumniResponse;
import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.entity.Alumni;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.AlumniRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alumni")
public class AlumniController {

    @Autowired
    private AlumniRepository alumniRepository;

    /** PUBLIC — no token needed */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlumniResponse>>> getAll(
            @RequestParam(required = false) String category) {

        List<Alumni> list = (category != null && !category.isBlank())
                ? alumniRepository.findByCategoryAndActiveTrueOrderByCreatedAtDesc(category)
                : alumniRepository.findByActiveTrueOrderByCreatedAtDesc();

        return ResponseEntity.ok(ApiResponse.ok(list.stream().map(this::toResponse).collect(Collectors.toList()),
                "Alumni fetched successfully"));
    }

    /** PUBLIC — get single alumni */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlumniResponse>> getById(@PathVariable Long id) {
        Alumni a = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni", id));
        return ResponseEntity.ok(ApiResponse.ok(toResponse(a), "Alumni fetched"));
    }

    /** ADMIN ONLY */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AlumniResponse>> add(@Valid @RequestBody AlumniRequest req) {
        Alumni a = Alumni.builder()
                .name(req.getName()).batch(req.getBatch())
                .degree(req.getDegree()).institution(req.getInstitution())
                .currentRole(req.getCurrentRole()).achievement(req.getAchievement())
                .img(req.getImg()).category(req.getCategory() != null ? req.getCategory() : "industry")
                .linkedIn(req.getLinkedIn()).active(true)
                .build();
        Alumni saved = alumniRepository.save(a);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(toResponse(saved), "Alumni added successfully"));
    }

    /** ADMIN ONLY */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AlumniResponse>> update(
            @PathVariable Long id, @Valid @RequestBody AlumniRequest req) {
        Alumni a = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni", id));
        a.setName(req.getName());
        a.setBatch(req.getBatch());
        a.setDegree(req.getDegree());
        a.setInstitution(req.getInstitution());
        a.setCurrentRole(req.getCurrentRole());
        a.setAchievement(req.getAchievement());
        if (req.getImg() != null) a.setImg(req.getImg());
        if (req.getCategory() != null) a.setCategory(req.getCategory());
        a.setLinkedIn(req.getLinkedIn());
        Alumni saved = alumniRepository.save(a);
        return ResponseEntity.ok(ApiResponse.ok(toResponse(saved), "Alumni updated"));
    }

    /** ADMIN ONLY */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        Alumni a = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni", id));
        a.setActive(false);
        alumniRepository.save(a);
        return ResponseEntity.ok(ApiResponse.ok("Alumni deleted"));
    }

    private AlumniResponse toResponse(Alumni a) {
        return AlumniResponse.builder()
                .id(a.getId()).name(a.getName()).batch(a.getBatch())
                .degree(a.getDegree()).institution(a.getInstitution())
                .currentRole(a.getCurrentRole()).achievement(a.getAchievement())
                .img(a.getImg()).category(a.getCategory())
                .linkedIn(a.getLinkedIn()).createdAt(a.getCreatedAt())
                .build();
    }
}
