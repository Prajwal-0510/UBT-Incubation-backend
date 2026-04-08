package com.ubt.backend.service.impl;

import com.ubt.backend.dto.ProjectRequest;
import com.ubt.backend.dto.ProjectResponse;
import com.ubt.backend.entity.Project;
import com.ubt.backend.exception.BadRequestException;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.ProjectRepository;
import com.ubt.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final List<String> VALID_LEVELS =
            List.of("PhD R&D", "ME/MTech", "BE/BTech", "MBA Projects");

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByLevel(String level) {
        if (!VALID_LEVELS.contains(level)) {
            throw new BadRequestException("Invalid level. Must be one of: " + VALID_LEVELS);
        }
        return projectRepository.findByLevelAndActiveTrueOrderByCreatedAtDesc(level)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse addProject(ProjectRequest request) {
        if (!VALID_LEVELS.contains(request.getLevel())) {
            throw new BadRequestException("Invalid level. Must be one of: " + VALID_LEVELS);
        }

        // Convert tech list to comma-separated string for storage
        String techStack = null;
        if (request.getTech() != null && !request.getTech().isEmpty()) {
            techStack = String.join(",", request.getTech());
        }

        Project project = Project.builder()
                .title(request.getTitle())
                .domain(request.getDomain())
                .level(request.getLevel())
                .img(request.getImg())
                .description(request.getDescription())
                .techStack(techStack)
                .duration(request.getDuration())
                .date(request.getDate())
                .active(true)
                .build();

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        // Soft delete
        project.setActive(false);
        projectRepository.save(project);
    }

    private ProjectResponse toResponse(Project p) {
        // Convert comma-separated tech stack back to list
        List<String> techList = (p.getTechStack() != null && !p.getTechStack().isBlank())
                ? Arrays.asList(p.getTechStack().split(","))
                : List.of();

        return ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .domain(p.getDomain())
                .level(p.getLevel())
                .img(p.getImg())
                .desc(p.getDescription())
                .tech(techList)
                .duration(p.getDuration())
                .date(p.getDate())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
