package com.nursetrack.web.controller.supervisor;

import com.nursetrack.domain.model.User;
import com.nursetrack.service.DepartmentService;
import com.nursetrack.service.NurseDepartmentService;
import com.nursetrack.service.ShiftService;
import com.nursetrack.service.SupervisorDepartmentService;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.request.shift.CreateShiftRequest;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import com.nursetrack.web.dto.response.ShiftResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/supervisor/departments/{departmentId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERVISOR')")
public class SupervisorDepartmentController
{
    private final DepartmentService departmentService;
    private final SupervisorDepartmentService supervisorDepartmentService;
    private final NurseDepartmentService nurseDepartmentService;
    private final ShiftService shiftService;

    @GetMapping
    public ResponseEntity<DepartmentResponse> getMyDepartment(@PathVariable("departmentId") Long departmentId,
                                                              @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    // ==================== ENFERMERAS DEL DEPARTAMENTO ====================

    @GetMapping("/nurses")
    public ResponseEntity<List<NurseDepartmentResponse>> getDepartmentNurses(@PathVariable("departmentId") Long departmentId,
                                                                             @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(nurseDepartmentService.getByDepartmentId(departmentId));
    }

    @PostMapping("/nurses")
    public ResponseEntity<NurseDepartmentResponse> addNurseToDepartment(@PathVariable("departmentId") Long departmentId,
                                                                        AssignNurseRequest request,
                                                                        @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(nurseDepartmentService.assignNurseToDepartment(request));
    }

    @DeleteMapping("/nurses/{nurseId}")
    public ResponseEntity<Void> removeNurseFromDepartment(@PathVariable("departmentId") Long departmentId,
                                                          @PathVariable("nurseId") Long nurseId,
                                                          @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        nurseDepartmentService.removeNurseFromDepartment(nurseId, departmentId);
        return ResponseEntity.noContent().build();
    }

    // ==================== TURNOS DEL DEPARTAMENTO ====================

    @GetMapping("/shifts")
    public ResponseEntity<List<ShiftResponse>> getDepartmentShifts(@PathVariable("departmentId") Long departmentId,
                                                                   @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(shiftService.getAllShiftsByDepartmentId(departmentId));
    }

    @PostMapping("/shifts")
    public ResponseEntity<ShiftResponse> createShift(@PathVariable("departmentId") Long departmentId,
                                                     @Valid @RequestBody CreateShiftRequest request,
                                                     @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        request.setCreatedById(supervisor.getId());
        request.setDepartmentId(departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.createShift(request));
    }

    @PutMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(@PathVariable("departmentId") Long departmentId,
                                                     @PathVariable("shiftId") Long shiftId,
                                                     @Valid @RequestBody UpdateShiftRequest request,
                                                     @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(shiftService.updateShift(shiftId, request));
    }

    @DeleteMapping("/shifts/{shiftId}")
    public ResponseEntity<Void> cancelShift(@PathVariable("departmentId") Long departmentId,
                                            @PathVariable("shiftId") Long shiftId,
                                            @AuthenticationPrincipal User supervisor)
            throws AccessDeniedException
    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }
}