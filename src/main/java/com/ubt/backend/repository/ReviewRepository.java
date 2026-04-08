package com.ubt.backend.repository;

import com.ubt.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByActiveTrueOrderByCreatedAtDesc();
    List<Review> findByRatingAndActiveTrueOrderByCreatedAtDesc(Integer rating);
}