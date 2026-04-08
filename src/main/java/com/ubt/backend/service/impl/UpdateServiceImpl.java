package com.ubt.backend.service.impl;

import com.ubt.backend.dto.UpdateRequest;
import com.ubt.backend.dto.UpdateResponse;
import com.ubt.backend.entity.Update;
import com.ubt.backend.exception.BadRequestException;
import com.ubt.backend.exception.ResourceNotFoundException;
import com.ubt.backend.repository.UpdateRepository;
import com.ubt.backend.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UpdateServiceImpl implements UpdateService {

    private static final List<String> VALID_TYPES =
            List.of("announcement", "achievement", "event", "news");

    @Autowired
    private UpdateRepository updateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UpdateResponse> getAllUpdates() {
        return updateRepository.findAllActiveOrderByPinnedAndDate()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UpdateResponse addUpdate(UpdateRequest request) {
        if (!VALID_TYPES.contains(request.getType())) {
            throw new BadRequestException("Invalid type. Must be one of: " + VALID_TYPES);
        }

        Update update = Update.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .pinned(request.getPinned() != null ? request.getPinned() : false)
                .active(true)
                .build();

        Update saved = updateRepository.save(update);
        return toResponse(saved);
    }

    @Override
    public void deleteUpdate(Long id) {
        Update update = updateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Update", id));
        update.setActive(false);
        updateRepository.save(update);
    }

    @Override
    public UpdateResponse togglePin(Long id) {
        Update update = updateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Update", id));
        update.setPinned(!update.getPinned());
        Update saved = updateRepository.save(update);
        return toResponse(saved);
    }

    private UpdateResponse toResponse(Update u) {
        return UpdateResponse.builder()
                .id(u.getId())
                .type(u.getType())
                .title(u.getTitle())
                .content(u.getContent())
                .date(u.getDate())
                .pinned(u.getPinned())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
