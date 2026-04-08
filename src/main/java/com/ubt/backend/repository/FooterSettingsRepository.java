package com.ubt.backend.repository;

import com.ubt.backend.entity.FooterSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterSettingsRepository extends JpaRepository<FooterSettings, Long> {
    // Always use the single row with id=1
}
