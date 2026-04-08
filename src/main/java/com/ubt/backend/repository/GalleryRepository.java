package com.ubt.backend.repository;

import com.ubt.backend.entity.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryItem, Long> {

    List<GalleryItem> findByActiveTrueOrderByCreatedAtDesc();

    List<GalleryItem> findByCategoryAndActiveTrueOrderByCreatedAtDesc(String category);

    List<GalleryItem> findByActiveTrue();
}
