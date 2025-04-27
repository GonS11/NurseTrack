package com.nursetrack.service;

import com.nursetrack.domain.model.NurseDepartment;
import com.nursetrack.domain.model.User;
import com.nursetrack.exception.AssignmentException;
import com.nursetrack.exception.NurseAssignmentNotFoundException;
import com.nursetrack.repository.NurseDepartmentRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import com.nursetrack.web.mappers.DepartmentMapper;
import com.nursetrack.web.mappers.NurseDepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NurseDepartmentService
{
    private final NurseDepartmentRepository nurseDepartmentRepository;
    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;
    private final NurseDepartmentMapper nurseDepartmentMapper;

    @Transactional(readOnly = true)
    public List<NurseDepartmentResponse> getByDepartmentId(Long departmentId)
    {
        return nurseDepartmentMapper.toDtoList(nurseDepartmentRepository.findByDepartmentIdList(departmentId));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByNurseId(Long nurseId)
    {
        return nurseDepartmentRepository.findByNurseIdList(nurseId)
                .stream()
                .map(NurseDepartment::getDepartment)
                .map(departmentMapper::toDto)
                .toList();
    }

    public NurseDepartmentResponse assignNurseToDepartment(AssignNurseRequest request)
    {
        // Validar que la enfermera existe y es realmente una enfermera (Ya en DTO)
        // Validar existe departament (Ya en DTO)
        // Validar que la signacion no exista aun (En anotacion)

        NurseDepartment assignment =nurseDepartmentMapper.toEntity(request);

        // Evitar duplicados
        if (nurseDepartmentRepository.existsByNurseIdAndDepartmentId(request.getNurseId(), request.getDepartmentId()))
        {
            throw new AssignmentException("Nurse already assigned to department");
        }

        return nurseDepartmentMapper.toDTO(nurseDepartmentRepository.save(assignment));
    }


    public void removeNurseFromDepartment(Long nurseId, Long departmentId)
    {
        if (!nurseDepartmentRepository.existsByNurseIdAndDepartmentId(nurseId, departmentId))
        {
            throw new NurseAssignmentNotFoundException(nurseId, departmentId);
        }
        nurseDepartmentRepository.deleteByNurseIdAndDepartmentId(nurseId, departmentId);
    }

    public void validateNurseAccess(Long nurseId, Long departmentId)
            throws AccessDeniedException
    {
        if(!nurseDepartmentRepository.existsByNurseIdAndDepartmentId(nurseId, departmentId))
        {
            throw new AccessDeniedException("Nurse has not access to this department");
        }
    }

    public void validateNurseIdentity(User currentUser, Long nurseId) throws AccessDeniedException
    {
        if (!currentUser.getId().equals(nurseId))
        {
            throw new AccessDeniedException("Cannot access other nurse's data");
        }
    }
}
