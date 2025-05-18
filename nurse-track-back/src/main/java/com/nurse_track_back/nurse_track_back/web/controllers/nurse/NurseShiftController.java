package com.nurse_track_back.nurse_track_back.web.controllers.nurse;

import com.nurse_track_back.nurse_track_back.services.DepartmentService;
import com.nurse_track_back.nurse_track_back.services.NurseDepartmentService;
import com.nurse_track_back.nurse_track_back.services.ShiftService;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/nurses/{nurseId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('NURSE') and #nurseId == principal.id")
public class NurseShiftController
{
    private final NurseDepartmentService nurseDepartmentService;
    private final ShiftService shiftService;
    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getNurseDepartments(@PathVariable("nurseId") Long nurseId)
    {
        return ResponseEntity.ok(departmentService.getDepartmentsForNurse(nurseId));
    }

    @GetMapping("/shifts")
    public ResponseEntity<List<ShiftResponse>> getNurseShifts(@PathVariable("nurseId") Long nurseId)
    {
        return ResponseEntity.ok(shiftService.getAllShiftByUserId(nurseId));
    }

    @GetMapping("/departments/{departmentId}/shifts")
    public ResponseEntity<List<ShiftResponse>> getDepartmentShiftsForNurse(@PathVariable("nurseId") Long nurseId,
                                                                           @PathVariable("departmentId") Long departmentId)
    {
        nurseDepartmentService.validateNurseDepartmentAssociation(nurseId, departmentId);
        return ResponseEntity.ok(shiftService.getShiftsByNurseAndDepartment(nurseId, departmentId));
    }
}