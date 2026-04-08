package com.ubt.backend.repository;

import com.ubt.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByActiveTrueOrderByCreatedAtDesc();

    List<Project> findByLevelAndActiveTrueOrderByCreatedAtDesc(String level);

    List<Project> findByActiveTrue();
}
