package com.nurse_track_back.nurse_track_back.service;

import com.nurse_track_back.nurse_track_back.domain.model.ShiftTemplate;
import com.nurse_track_back.nurse_track_back.dto.response.ShiftTemplateResponse;
import com.nurse_track_back.nurse_track_back.exception.ShiftTemplateNotFoundException;
import com.nurse_track_back.nurse_track_back.mapper.ShiftTemplateMapper;
import com.nurse_track_back.nurse_track_back.repository.ShiftTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftTemplateService
{
    private final ShiftTemplateRepository shiftTemplateRepository;
    private final ShiftTemplateMapper shiftTemplateMapper;

    @Transactional(readOnly = true)
    public List<ShiftTemplateResponse> getAllTemplates()
    {
        return shiftTemplateMapper.toDTOList(shiftTemplateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ShiftTemplateResponse getTemplateById(Long id)
    {
        ShiftTemplate shiftTemplate = shiftTemplateRepository.findById(id)
                .orElseThrow(() -> new ShiftTemplateNotFoundException(id));

        return shiftTemplateMapper.toDTO(shiftTemplate);
    }
}
