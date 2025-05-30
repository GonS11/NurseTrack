package com.nurse_track_back.nurse_track_back.web.controllers.supervisor;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.services.DepartmentService;
import com.nurse_track_back.nurse_track_back.services.NurseDepartmentService;
import com.nurse_track_back.nurse_track_back.services.ShiftService;
import com.nurse_track_back.nurse_track_back.services.SupervisorDepartmentService;
import com.nurse_track_back.nurse_track_back.services.UserService;
import com.nurse_track_back.nurse_track_back.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.CreateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.UpdateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.NurseDepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/supervisor/departments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERVISOR')")
public class SupervisorDepartmentController {
    private final DepartmentService departmentService;
    private final SupervisorDepartmentService supervisorDepartmentService;
    private final NurseDepartmentService nurseDepartmentService;
    private final ShiftService shiftService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<DepartmentResponse>> getAllMyDepartments(@AuthenticationPrincipal User supervisor) {
        return ResponseEntity.ok(departmentService.getAllUserDepartments(supervisor.getId()));
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> getMyDepartment(@PathVariable("departmentId") Long departmentId,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    // ==================== ENFERMERAS DEL DEPARTAMENTO ====================
    @GetMapping("/nurses/all")
    public ResponseEntity<List<UserResponse>> getAllNurses() {
        List<UserResponse> allUsers = userService.getAllUsers(0, 1000, "id").getContent(); // Fetch a reasonable number
                                                                                           // or implement pagination
        List<UserResponse> nurses = allUsers.stream()
                .filter(user -> user.getRole() == Role.ROLE_NURSE)
                .collect(Collectors.toList());
        return ResponseEntity.ok(nurses);
    }

    @GetMapping("/{departmentId}/nurses")
    public ResponseEntity<List<NurseDepartmentResponse>> getDepartmentNurses(
            @PathVariable("departmentId") Long departmentId,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(nurseDepartmentService.getByDepartmentId(departmentId));
    }

    @PostMapping("/{departmentId}/nurses")
    public ResponseEntity<NurseDepartmentResponse> addNurseToDepartment(@PathVariable("departmentId") Long departmentId,
            @Valid @RequestBody AssignNurseRequest request,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(nurseDepartmentService.assignNurseToDepartment(request));
    }

    @DeleteMapping("/{departmentId}/nurses/{nurseId}")
    public ResponseEntity<Void> removeNurseFromDepartment(@PathVariable("departmentId") Long departmentId,
            @PathVariable("nurseId") Long nurseId,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        nurseDepartmentService.removeNurseFromDepartment(nurseId, departmentId);
        return ResponseEntity.noContent().build();
    }

    // ==================== TURNOS DEL DEPARTAMENTO ====================
    @GetMapping("/{departmentId}/shifts")
    public ResponseEntity<List<ShiftResponse>> getDepartmentShifts(@PathVariable("departmentId") Long departmentId,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(shiftService.getAllShiftsByDepartmentId(departmentId));
    }

    @PostMapping("/{departmentId}/shifts")
    public ResponseEntity<ShiftResponse> createShift(@PathVariable("departmentId") Long departmentId,
            @Valid @RequestBody CreateShiftRequest request,
            @AuthenticationPrincipal User supervisor) {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        request.setCreatedById(supervisor.getId());
        request.setDepartmentId(departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.createShift(request));
    }

    @PutMapping("/{departmentId}/shifts/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(@PathVariable("departmentId") Long departmentId,
            @PathVariable("shiftId") Long shiftId,
            @Valid @RequestBody UpdateShiftRequest request,
            @AuthenticationPrincipal User supervisor)

    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        return ResponseEntity.ok(shiftService.updateShift(shiftId, request, supervisor.getId()));
    }

    @DeleteMapping("/{departmentId}/shifts/{shiftId}")
    public ResponseEntity<Void> cancelShift(@PathVariable("departmentId") Long departmentId,
            @PathVariable("shiftId") Long shiftId,
            @AuthenticationPrincipal User supervisor)

    {
        supervisorDepartmentService.validateSupervisorAccess(supervisor.getId(), departmentId);

        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }
}
