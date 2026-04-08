package com.ubt.backend.repository;

import com.ubt.backend.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {
    List<Alumni> findByActiveTrueOrderByCreatedAtDesc();
    List<Alumni> findByCategoryAndActiveTrueOrderByCreatedAtDesc(String category);
}
