package com.ubt.backend.service;

import com.ubt.backend.dto.GalleryItemRequest;
import com.ubt.backend.dto.GalleryItemResponse;

import java.util.List;

public interface GalleryService {

    List<GalleryItemResponse> getAllItems();

    List<GalleryItemResponse> getItemsByCategory(String category);

    GalleryItemResponse addItem(GalleryItemRequest request);

    void deleteItem(Long id);
}
