package com.nursetrack.service;

import com.nursetrack.domain.model.Department;
import com.nursetrack.exception.DepartmentAlreadyExistsException;
import com.nursetrack.exception.DepartmentIsAlreadyActiveException;
import com.nursetrack.exception.DepartmentIsAlreadyInactiveException;
import com.nursetrack.exception.DepartmentNotFoundException;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.web.dto.request.department.CreateDepartmentRequest;
import com.nursetrack.web.dto.request.department.UpdateDepartmentRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import com.nursetrack.web.mappers.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService
{
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments()
    {
        return departmentMapper.toDTOList(departmentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartmentsByUserId(Long nurseId)
    {
        return departmentMapper.toDTOList(departmentRepository.findAllByUserId(nurseId));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllActiveDepartments()
    {
        return departmentMapper.toDTOList(departmentRepository.findByIsActiveTrue());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        return departmentMapper.toDto(department);
    }


    public DepartmentResponse createDepartment(CreateDepartmentRequest request)
    {
        validateName(request.getName());

        Department department = departmentMapper.toModel(request);
        department.setIsActive(true);

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }


    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

       departmentMapper.updateModel(request, department);

       if(request.getName() != null)
       {
           validateName(department.getName());
       }

       Department updateDepartment = departmentRepository.save(department);

       return departmentMapper.toDto(updateDepartment);
    }


    public void deleteDepartment(Long id)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        departmentRepository.delete(department);
    }


    public void deactivateDepartment(Long id)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        if(!department.getIsActive())
        {
            throw new DepartmentIsAlreadyInactiveException(id);
        }

        department.setIsActive(false);
        departmentRepository.save(department);
    }


    public void activateDepartment(Long id)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        if(department.getIsActive())
        {
            throw new DepartmentIsAlreadyActiveException(id);
        }

        department.setIsActive(true);
        departmentRepository.save(department);
    }

    private void validateName(String name)
            throws DepartmentAlreadyExistsException
    {
        if(departmentRepository.existsByName(name)) throw new DepartmentAlreadyExistsException(name);
    }
}
