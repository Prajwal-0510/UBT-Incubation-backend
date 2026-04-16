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

    // ── READ ──────────────────────────────────────────────────────────────────

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

    // ── WRITE ─────────────────────────────────────────────────────────────────

    @Override
    public ProjectResponse addProject(ProjectRequest request) {
        if (!VALID_LEVELS.contains(request.getLevel())) {
            throw new BadRequestException("Invalid level. Must be one of: " + VALID_LEVELS);
        }

        // Convert tech list → comma-separated string for DB storage
        String techStack = null;
        if (request.getTech() != null && !request.getTech().isEmpty()) {
            techStack = request.getTech().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(","));
        }

        Project project = Project.builder()
                .title(request.getTitle().trim())
                .domain(request.getDomain())
                .level(request.getLevel())
                .imgUrl(request.getImg())           // frontend field: img  → entity field: imgUrl
                .description(request.getDesc())     // frontend field: desc → entity field: description
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
        // Soft delete — keeps record in DB, just hides it from public
        project.setActive(false);
        projectRepository.save(project);
    }

    // ── MAPPER ────────────────────────────────────────────────────────────────

    /**
     * Converts Project entity → ProjectResponse DTO.
     *
     * Key mappings:
     *   entity.imgUrl       → response.img
     *   entity.description  → response.desc
     *   entity.techStack    → response.tech (List<String>)
     */
    private ProjectResponse toResponse(Project p) {
        List<String> techList = (p.getTechStack() != null && !p.getTechStack().isBlank())
                ? Arrays.stream(p.getTechStack().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList())
                : List.of();

        return ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .domain(p.getDomain())
                .level(p.getLevel())
                .img(p.getImgUrl())           // entity imgUrl → DTO img
                .desc(p.getDescription())     // entity description → DTO desc
                .tech(techList)
                .duration(p.getDuration())
                .date(p.getDate())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
