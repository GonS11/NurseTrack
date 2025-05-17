package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.ShiftTemplate;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftTemplateResponse;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.web.mappers.ShiftTemplateMapper;
import com.nurse_track_back.nurse_track_back.repositories.ShiftTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftTemplateService {
    private final ShiftTemplateRepository shiftTemplateRepository;
    private final ShiftTemplateMapper shiftTemplateMapper;

    @Transactional(readOnly = true)
    public List<ShiftTemplateResponse> getAllTemplates() {
        return shiftTemplateMapper.toDTOList(shiftTemplateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ShiftTemplateResponse getTemplateById(Long id) {
        ShiftTemplate shiftTemplate = shiftTemplateRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift template", id));

        return shiftTemplateMapper.toDTO(shiftTemplate);
    }
}
