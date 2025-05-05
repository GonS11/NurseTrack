package com.nursetrack.web.controller.admin;

import com.nursetrack.service.NurseDepartmentService;
import com.nursetrack.service.SupervisorDepartmentService;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import com.nursetrack.web.dto.response.SupervisorDepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/assignments/departments/{departmentId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAssignmentController
{
    private final SupervisorDepartmentService supervisorDepartmentService;
    private final NurseDepartmentService nurseDepartmentService;

    // ==============================================
    // ASIGNACIÓN DE SUPERVISORES (1:1 con departamento)
    // ==============================================

    @GetMapping("/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> getDepartmentSupervisor(@PathVariable("departmentId") Long departmentId)
    {
        return ResponseEntity.ok(supervisorDepartmentService.getByDepartmentId(departmentId));
    }

    @PostMapping("/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> assignSupervisor(@Valid @RequestBody AssignSupervisorRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(supervisorDepartmentService.assignSupervisor(request));
    }

    @DeleteMapping("/supervisor")
    public ResponseEntity<Void> removeSupervisorFromDepartment(@PathVariable("departmentId") Long departmentId)
    {
        supervisorDepartmentService.removeSupervisor(departmentId);
        return ResponseEntity.noContent().build();
    }

    // ==============================================
    // ASIGNACIÓN DE ENFERMERAS (N:M con departamentos)
    // ==============================================

    @GetMapping("/nurses")
    public ResponseEntity<List<NurseDepartmentResponse>> getNursesByDepartment(@PathVariable("departmentId") Long departmentId)
    {
        return ResponseEntity.ok(nurseDepartmentService.getByDepartmentId(departmentId));
    }

    @PostMapping("/nurses")
    public ResponseEntity<NurseDepartmentResponse> assignNurseToDepartment(@Valid @RequestBody AssignNurseRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(nurseDepartmentService.assignNurseToDepartment(request));
    }

    @DeleteMapping("/nurses/{nurseId}")
    public ResponseEntity<Void> removeNurseFromDepartment(@PathVariable Long departmentId,
                                                          @PathVariable Long nurseId)
    {
        nurseDepartmentService.removeNurseFromDepartment(nurseId, departmentId);
        return ResponseEntity.noContent().build();
    }
}