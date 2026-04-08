package com.ubt.backend.repository;

import com.ubt.backend.entity.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateRepository extends JpaRepository<Update, Long> {

    // Pinned first, then by date desc
    @Query("SELECT u FROM Update u WHERE u.active = true ORDER BY u.pinned DESC, u.date DESC")
    List<Update> findAllActiveOrderByPinnedAndDate();

    List<Update> findByTypeAndActiveTrueOrderByDateDesc(String type);
}
