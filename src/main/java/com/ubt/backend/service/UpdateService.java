package com.ubt.backend.service;

import com.ubt.backend.dto.UpdateRequest;
import com.ubt.backend.dto.UpdateResponse;

import java.util.List;

public interface UpdateService {

    List<UpdateResponse> getAllUpdates();

    UpdateResponse addUpdate(UpdateRequest request);

    void deleteUpdate(Long id);

    UpdateResponse togglePin(Long id);
}
