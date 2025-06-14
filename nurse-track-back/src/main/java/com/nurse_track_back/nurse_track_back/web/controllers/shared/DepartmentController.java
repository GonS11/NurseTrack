package com.nurse_track_back.nurse_track_back.web.controllers.shared;

import com.nurse_track_back.nurse_track_back.services.ShiftService;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final ShiftService shiftService;

    @GetMapping("/{departmentId}/shifts/all")
    public ResponseEntity<List<ShiftResponse>> getAllShiftsForDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(shiftService.getAllShiftsByDepartmentId(departmentId));
    }
}
