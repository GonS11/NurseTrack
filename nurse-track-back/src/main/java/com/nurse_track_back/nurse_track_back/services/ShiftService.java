package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.*;
import com.nurse_track_back.nurse_track_back.utils.ShiftAuthorizationValidator;
import com.nurse_track_back.nurse_track_back.utils.ShiftBusinessValidator;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.CreateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.UpdateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.ShiftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ShiftBusinessValidator businessValidator;
    private final ShiftAuthorizationValidator authValidator;
    private final NurseDepartmentService nurseDepartmentService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ShiftTemplateRepository shiftTemplateRepository;

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftsByDepartmentId(Long departmentId) {
        List<Shift> shifts = shiftRepository.findAllByDepartmentId(departmentId);
        if (shifts == null) {
            return List.of();
        }
        return shiftMapper.toDTOList(shifts);
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftByUserId(Long nurseId) {
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseId(nurseId));
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShiftByUserIdAndDate(Long nurseId, LocalDate startDate, LocalDate endDate) {
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseIdAndShiftDateBetween(nurseId, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsByNurseAndDepartment(Long nurseId, Long departmentId) {
        nurseDepartmentService.validateNurseAccess(nurseId, departmentId);
        return shiftMapper.toDTOList(shiftRepository.findAllByNurseIdAndDepartmentId(nurseId, departmentId));

    }

    @Transactional(readOnly = true)
    public ShiftResponse getShiftById(Long shiftId, Long departmentId) {
        Shift shift = shiftRepository.findByIdAndDepartmentId(shiftId, departmentId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift", shiftId));

        return shiftMapper.toDTO(shift);
    }

    public ShiftResponse createShift(CreateShiftRequest request) {
        Shift shift = shiftMapper.toEntity(request, userRepository, departmentRepository, shiftTemplateRepository);

        Shift savedShift = shiftRepository.save(shift);
        return shiftMapper.toDTO(savedShift);
    }

    public ShiftResponse updateShift(Long shiftId, UpdateShiftRequest request, Long supervisorId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift", shiftId));

        authValidator.validateSupervisorShiftAccess(supervisorId, shift, request.getDepartmentId());
        businessValidator.validateUpdate(shift, request);

        shiftMapper.updateModel(request, shift, userRepository, departmentRepository, shiftTemplateRepository);
        Shift updatedShift = shiftRepository.save(shift);
        return shiftMapper.toDTO(updatedShift);
    }

    public void deleteShift(Long shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw ResourceNotFoundException.create("Shift", shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }

}
