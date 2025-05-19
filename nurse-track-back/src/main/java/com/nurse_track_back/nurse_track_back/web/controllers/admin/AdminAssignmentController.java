package com.nurse_track_back.nurse_track_back.web.controllers.admin;

import com.nurse_track_back.nurse_track_back.services.NurseDepartmentService;
import com.nurse_track_back.nurse_track_back.services.SupervisorDepartmentService;
import com.nurse_track_back.nurse_track_back.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.NurseDepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.SupervisorDepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/assignments/departments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAssignmentController {
    private final SupervisorDepartmentService supervisorDepartmentService;
    private final NurseDepartmentService nurseDepartmentService;

    // ==============================================
    // ASIGNACIÓN DE SUPERVISORES (1:1 con departamento)
    // ==============================================

    @GetMapping()
    public ResponseEntity<Page<SupervisorDepartmentResponse>> getAllSupervisorAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departmentId") String sortBy) {
        return ResponseEntity.ok(supervisorDepartmentService.getAllAssignments(page, size, sortBy));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<DepartmentResponse>> getUnassignedDepartmentsForSupervisor() {
        return ResponseEntity.ok(supervisorDepartmentService.getAllUnassignedDepartments());
    }

    /**
     * Obtener el supervisor asignado a un departamento.
     */
    @GetMapping("/{departmentId}/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> getDepartmentSupervisor(
            @PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(supervisorDepartmentService.getByDepartmentId(departmentId));
    }

    /**
     * Asignar un supervisor a un departamento.
     */
    @PostMapping("/{departmentId}/supervisor")
    public ResponseEntity<SupervisorDepartmentResponse> assignSupervisor(
            @Valid @RequestBody AssignSupervisorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supervisorDepartmentService.assignSupervisor(request));
    }

    /**
     * Eliminar el supervisor asignado a un departamento.
     */
    @DeleteMapping("/{departmentId}/supervisor")
    public ResponseEntity<Void> removeSupervisorFromDepartment(@PathVariable("departmentId") Long departmentId) {
        supervisorDepartmentService.removeSupervisor(departmentId);
        return ResponseEntity.noContent().build();
    }

    // ==============================================
    // ASIGNACIÓN DE ENFERMERAS (N:M con departamentos)
    // ==============================================

    @GetMapping("/nurses")
    public ResponseEntity<Page<NurseDepartmentResponse>> getAllNurseAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departmentId") String sortBy) {
        return ResponseEntity.ok(nurseDepartmentService.getAllAssignments(page, size, sortBy));
    }

    @GetMapping("/nurses/unassigned") // Corregido el path (faltaba /)
    public ResponseEntity<List<DepartmentResponse>> getUnassignedDepartmentsForNurses() {
        return ResponseEntity.ok(nurseDepartmentService.getAllUnassignedDepartments());
    }

    /**
     * Obtener todas las enfermeras asignadas a un departamento.
     */
    @GetMapping("/{departmentId}/nurses")
    public ResponseEntity<List<NurseDepartmentResponse>> getNursesByDepartment(
            @PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(nurseDepartmentService.getByDepartmentId(departmentId));
    }

    /**
     * Asignar una enfermera a un departamento.
     */
    @PostMapping("/{departmentId}/nurses")
    public ResponseEntity<NurseDepartmentResponse> assignNurseToDepartment(
            @Valid @RequestBody AssignNurseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nurseDepartmentService.assignNurseToDepartment(request));
    }

    /**
     * Eliminar una enfermera de un departamento.
     */
    @DeleteMapping("/{departmentId}/nurses/{nurseId}")
    public ResponseEntity<Void> removeNurseFromDepartment(@PathVariable("departmentId") Long departmentId,
            @PathVariable("nurseId") Long nurseId) {
        nurseDepartmentService.removeNurseFromDepartment(nurseId, departmentId);
        return ResponseEntity.noContent().build();
    }
}
