package com.nursetrack.web.controller.admin;

import com.nursetrack.service.DepartmentService;
import com.nursetrack.web.dto.request.department.CreateDepartmentRequest;
import com.nursetrack.web.dto.request.department.UpdateDepartmentRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminDepartmentController
{
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments()
    {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DepartmentResponse>> getAllActiveDepartments()
    {
        return ResponseEntity.ok(departmentService.getAllActiveDepartments());
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("departmentId") Long departmentId)
    {
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody CreateDepartmentRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(request));
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable("departmentId") Long departmentId,
                                                               @Valid @RequestBody UpdateDepartmentRequest request)
    {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, request));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("departmentId") Long departmentId)
    {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{departmentId}/desactivate")
    public ResponseEntity<Void> deactivateDepartment(@PathVariable("departmentId") Long departmentId)
    {
        departmentService.deactivateDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{departmentId}/activate")
    public ResponseEntity<Void> activateDepartment(@PathVariable("departmentId") Long departmentId)
    {
        departmentService.activateDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}
