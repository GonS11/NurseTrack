package com.nursetrack.service;

import com.nursetrack.exception.ShiftTemplateNotFoundException;
import com.nursetrack.repository.ShiftTemplateRepository;
import com.nursetrack.web.dto.response.ShiftTemplateResponse;
import com.nursetrack.web.mappers.ShiftTemplateMapper;
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
        return shiftTemplateMapper.toDtoList(shiftTemplateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ShiftTemplateResponse getEntityById(Long id)
    {
        return shiftTemplateRepository.findById(id)
                .map(shiftTemplateMapper::toDto)
                .orElseThrow(() -> new ShiftTemplateNotFoundException(id));
    }
}