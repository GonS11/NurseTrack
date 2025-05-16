package com.nurse_track_back.nurse_track_back.controller.admin;

import com.nurse_track_back.nurse_track_back.dto.request.department.CreateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.dto.request.department.UpdateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDepartmentController
{
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(departmentService.getAllDepartments(page, size, sortBy));
    }


    @GetMapping("/active")
    public ResponseEntity<List<DepartmentResponse>> getAllActiveDepartments()
    {
        return ResponseEntity.ok(departmentService.getAllActiveDepartments());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<DepartmentResponse>> getAllInactiveDepartments()
    {
        return ResponseEntity.ok(departmentService.getAllInactiveDepartments());
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
        departmentService.desactivateDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{departmentId}/activate")
    public ResponseEntity<Void> activateDepartment(@PathVariable("departmentId") Long departmentId)
    {
        departmentService.activateDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}
