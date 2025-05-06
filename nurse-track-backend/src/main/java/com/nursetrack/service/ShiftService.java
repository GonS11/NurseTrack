package com.nursetrack.service;

import com.nursetrack.domain.model.Shift;
import com.nursetrack.exception.ShiftNotFoundException;
import com.nursetrack.repository.NurseDepartmentRepository;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.utils.ShiftValidation;
import com.nursetrack.web.dto.request.shift.CreateShiftRequest;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;
import com.nursetrack.web.dto.response.ShiftResponse;
import com.nursetrack.web.mappers.ShiftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShiftService
{
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final NurseDepartmentRepository nurseDepartmentRepository;
    private final NurseDepartmentService nurseDepartmentService;

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftsByDepartmentId(Long departmentId)
    {
        return shiftMapper.toDTOList(shiftRepository.findAllByDepartmentId(departmentId));
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftByUserId(Long nurseId)
    {
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseId(nurseId));
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftByUserIdAndDate(Long nurseId, LocalDate startDate, LocalDate endDate)
    {
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseIdAndShiftDateBetween(nurseId, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsByNurseAndDepartment(Long nurseId, Long departmentId)
            throws AccessDeniedException
    {
        nurseDepartmentService.validateNurseAccess(nurseId, departmentId);
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseIdAndDepartmentId(nurseId, departmentId));

    }

    @Transactional(readOnly = true)
    public ShiftResponse getShiftById(Long shiftId, Long departmentId)
    {
        Shift shift = shiftRepository.findByIdAndDepartmentId(shiftId, departmentId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        return shiftMapper.toDTO(shift);
    }


    public ShiftResponse createShift(CreateShiftRequest request)
    {
        Shift shift = shiftMapper.toEntity(request);

        Shift savedShift = shiftRepository.save(shift);
        return shiftMapper.toDTO(savedShift);
    }


    public ShiftResponse updateShift(Long shiftId, UpdateShiftRequest request)
    {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        ShiftValidation.validateShiftUpdate(shift, request, nurseDepartmentRepository, shiftRepository);

        shiftMapper.updateModel(request, shift);
        Shift updatedShift = shiftRepository.save(shift);
        return shiftMapper.toDTO(updatedShift);
    }

    public void deleteShift(Long shiftId)
    {
        if (!shiftRepository.existsById(shiftId))
        {
            throw new ShiftNotFoundException(shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }


}