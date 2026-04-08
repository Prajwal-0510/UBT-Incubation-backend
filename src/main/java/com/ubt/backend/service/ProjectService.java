package com.ubt.backend.service;

import com.ubt.backend.dto.ProjectRequest;
import com.ubt.backend.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {

    List<ProjectResponse> getAllProjects();

    List<ProjectResponse> getProjectsByLevel(String level);

    ProjectResponse addProject(ProjectRequest request);

    void deleteProject(Long id);
}
