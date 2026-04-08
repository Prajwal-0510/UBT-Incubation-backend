package com.ubt.backend.service.impl;

import com.ubt.backend.dto.GalleryItemRequest;
import com.ubt.backend.dto.GalleryItemResponse;
import com.ubt.backend.entity.GalleryItem;
import com.ubt.backend.exception.BadRequestException;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.GalleryRepository;
import com.ubt.backend.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private static final List<String> VALID_CATEGORIES = List.of("college-visit", "student-project");

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GalleryItemResponse> getAllItems() {
        return galleryRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryItemResponse> getItemsByCategory(String category) {
        if (!VALID_CATEGORIES.contains(category)) {
            throw new BadRequestException("Invalid category. Must be one of: " + VALID_CATEGORIES);
        }
        return galleryRepository.findByCategoryAndActiveTrueOrderByCreatedAtDesc(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GalleryItemResponse addItem(GalleryItemRequest request) {
        if (!VALID_CATEGORIES.contains(request.getCategory())) {
            throw new BadRequestException("Invalid category. Must be one of: " + VALID_CATEGORIES);
        }

        GalleryItem item = GalleryItem.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .img(request.getImg())
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .active(true)
                .build();

        GalleryItem saved = galleryRepository.save(item);
        return toResponse(saved);
    }

    @Override
    public void deleteItem(Long id) {
        GalleryItem item = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GalleryItem", id));
        // Soft delete
        item.setActive(false);
        galleryRepository.save(item);
    }

    private GalleryItemResponse toResponse(GalleryItem item) {
        return GalleryItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .category(item.getCategory())
                .img(item.getImg())
                .date(item.getDate())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
