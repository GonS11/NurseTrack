package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.domain.models.NurseDepartment;
import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.exceptions.*;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.NurseDepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.NurseDepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.DepartmentMapper;
import com.nurse_track_back.nurse_track_back.web.mappers.NurseDepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final NurseDepartmentMapper nurseDepartmentMapper;

    @Transactional(readOnly = true)
    public Page<NurseDepartmentResponse> getAllAssignments(int page, int size, String sortBy)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<NurseDepartment> assingmentsPage = nurseDepartmentRepository.findAll(pageable);

        // Mapear content del Page manualmente
        return assingmentsPage.map(nurseDepartmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllUnassignedDepartments()
    {
        List<Department> unassigned = departmentRepository.findNurseUnassignedDepartments();

        return departmentMapper.toDTOList(unassigned);
    }

    @Transactional(readOnly = true)
    public List<NurseDepartmentResponse> getByDepartmentId(Long departmentId)
    {
        return nurseDepartmentMapper.toDtoList(nurseDepartmentRepository.findAllByDepartmentId(departmentId));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByNurseId(Long nurseId)
    {
        List<NurseDepartment> nurseDepartments = nurseDepartmentRepository.findAllByNurseId(nurseId);

        return nurseDepartments
                .stream()
                .map(NurseDepartment::getDepartment)
                .map(departmentMapper::toDTO)
                .toList();
    }

    public NurseDepartmentResponse assignNurseToDepartment(AssignNurseRequest request)
    {
        User nurse = userRepository.getReferenceById(request.getNurseId());
        Department department = departmentRepository.getReferenceById(request.getDepartmentId());

        NurseDepartment assignment = NurseDepartment.builder()
                .nurse(nurse)
                .department(department)
                .build();

        NurseDepartment saved = nurseDepartmentRepository.save(assignment);
        return nurseDepartmentMapper.toDTO(saved);
    }


    public void removeNurseFromDepartment(Long nurseId, Long departmentId)
    {
        if (!nurseDepartmentRepository.existsByNurseIdAndDepartmentId(nurseId, departmentId))
        {
            throw AssignmentException.create("Nurse", nurseId,departmentId);
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

    public void validateNurseDepartmentAssociation(Long nurseId, Long departmentId)
            throws AccessDeniedException
    {
        if (!nurseDepartmentRepository.existsByNurseIdAndDepartmentId(nurseId, departmentId))
        {
            throw new AccessDeniedException("La enfermera no está asignada a este departamento");
        }
    }
}