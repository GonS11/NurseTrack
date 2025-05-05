package com.nursetrack.web.controller.nurse;

import com.nursetrack.domain.model.User;
import com.nursetrack.service.DepartmentService;
import com.nursetrack.service.NurseDepartmentService;
import com.nursetrack.service.ShiftService;
import com.nursetrack.web.dto.response.DepartmentResponse;
import com.nursetrack.web.dto.response.ShiftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('NURSE')")
public class NurseShiftController
{
    private final NurseDepartmentService nurseDepartmentService;
    private final ShiftService shiftService;
    private final DepartmentService departmentService;
    
    @GetMapping("/{nurseId}/departments")
    public ResponseEntity<List<DepartmentResponse>> getNurseDepartments(@PathVariable("nurseId") Long nurseId,
                                                                        @AuthenticationPrincipal User currentUser)
            throws AccessDeniedException
    {
        nurseDepartmentService.validateNurseIdentity(currentUser, nurseId);
        return ResponseEntity.ok(departmentService.getAllDepartmentsByUserId(nurseId));
    }

    @GetMapping("/{nurseId}/shifts")
    public ResponseEntity<List<ShiftResponse>> getNurseShifts(@PathVariable("nurseId") Long nurseId,
                                                              @AuthenticationPrincipal User currentUser)
            throws AccessDeniedException
    {
        nurseDepartmentService.validateNurseIdentity(currentUser, nurseId);
        return ResponseEntity.ok(shiftService.getAllShiftByUserId(nurseId));
    }

    @GetMapping("/{nurseId}/departments/{departmentId}/shifts")
    public ResponseEntity<List<ShiftResponse>> getDepartmentShiftsForNurse(@PathVariable("nurseId") Long nurseId,
                                                                           @PathVariable("departmentId") Long departmentId,
                                                                           @AuthenticationPrincipal User currentUser)
            throws AccessDeniedException
    {
        nurseDepartmentService.validateNurseIdentity(currentUser, nurseId);
        List<ShiftResponse> shifts = shiftService.getShiftsByUserIdAndDepartmentId(nurseId, departmentId);

        return ResponseEntity.ok(shifts);
    }
}