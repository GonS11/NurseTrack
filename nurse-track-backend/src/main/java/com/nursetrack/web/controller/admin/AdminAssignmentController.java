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
//@PreAuthorize("hasRole('ADMIN')")
public class AdminAssignmentController {

    private final SupervisorDepartmentService supervisorDepartmentService;
    private final NurseDepartmentService nurseDepartmentService;

    // ==============================================
    // ASIGNACIÓN DE SUPERVISORES (1:1 con departamento)
    // ==============================================

    /**
     * Obtener el supervisor asignado a un departamento.
     */
    @GetMapping("/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> getDepartmentSupervisor(
            @PathVariable("departmentId") Long departmentId) {
        SupervisorDepartmentResponse response = supervisorDepartmentService.getByDepartmentId(departmentId);
        return ResponseEntity.ok(response);
    }

    /**
     * Asignar un supervisor a un departamento.
     */
    @PostMapping("/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> assignSupervisor(
            @Valid @RequestBody AssignSupervisorRequest request) {
        SupervisorDepartmentResponse response = supervisorDepartmentService.assignSupervisor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Eliminar el supervisor asignado a un departamento.
     */
    @DeleteMapping("/supervisor")
    public ResponseEntity<Void> removeSupervisorFromDepartment(
            @PathVariable("departmentId") Long departmentId) {
        supervisorDepartmentService.removeSupervisor(departmentId);
        return ResponseEntity.noContent().build();
    }

    // ==============================================
    // ASIGNACIÓN DE ENFERMERAS (N:M con departamentos)
    // ==============================================

    /**
     * Obtener todas las enfermeras asignadas a un departamento.
     */
    @GetMapping("/nurses")
    public ResponseEntity<List<NurseDepartmentResponse>> getNursesByDepartment(
            @PathVariable("departmentId") Long departmentId) {
        List<NurseDepartmentResponse> responses = nurseDepartmentService.getByDepartmentId(departmentId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Asignar una enfermera a un departamento.
     */
    @PostMapping("/nurses")
    public ResponseEntity<NurseDepartmentResponse> assignNurseToDepartment(
            @Valid @RequestBody AssignNurseRequest request) {
        NurseDepartmentResponse response = nurseDepartmentService.assignNurseToDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Eliminar una enfermera de un departamento.
     */
    @DeleteMapping("/nurses/{nurseId}")
    public ResponseEntity<Void> removeNurseFromDepartment(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("nurseId") Long nurseId) {
        nurseDepartmentService.removeNurseFromDepartment(nurseId, departmentId);
        return ResponseEntity.noContent().build();
    }
}
