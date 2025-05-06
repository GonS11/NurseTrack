package com.nursetrack.web.controller.supervisor;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.User;
import com.nursetrack.service.ShiftChangeRequestService;
import com.nursetrack.service.VacationRequestService;
import com.nursetrack.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import com.nursetrack.web.dto.request.vacationRequest.UpdateVacationRequest;
import com.nursetrack.web.dto.response.ShiftChangeResponse;
import com.nursetrack.web.dto.response.VacationRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor/departments/{departmentId}/requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERVISOR')")
public class SupervisorRequestController
{
    private final VacationRequestService vacationRequestService;
    private final ShiftChangeRequestService shiftChangeRequestService;

    // ==================== VACATION REQUESTS ====================
    @GetMapping("/vacations/pending")
    public ResponseEntity<List<VacationRequestResponse>> getPendingVacationRequests(@PathVariable Long departmentId,
                                                                                    @AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(vacationRequestService.getPendingVacationRequestsByDepartment(departmentId, currentUser.getId()));
    }

    @GetMapping("/vacations")
    public ResponseEntity<List<VacationRequestResponse>> getAllVacationRequests(@PathVariable Long departmentId)
    {
        return ResponseEntity.ok(vacationRequestService.getVacationRequestsByDepartment(departmentId));
    }

    @PutMapping("/vacations/{requestId}/approve")
    public ResponseEntity<VacationRequestResponse> approveVacationRequest(@PathVariable Long requestId,
                                                                          @Valid @RequestBody UpdateVacationRequest request,
                                                                          @AuthenticationPrincipal User currentUser)
    {
        request.setStatus(Status.APPROVED);
        return ResponseEntity.ok(vacationRequestService.updateVacationRequestStatus(requestId, request, currentUser.getId()));
    }

    @PutMapping("/vacations/{requestId}/reject")
    public ResponseEntity<VacationRequestResponse> rejectVacationRequest(@PathVariable Long requestId,
                                                                         @Valid @RequestBody UpdateVacationRequest request,
                                                                         @AuthenticationPrincipal User currentUser)
    {
        request.setStatus(Status.REJECTED);
        return ResponseEntity.ok(vacationRequestService.updateVacationRequestStatus(requestId, request, currentUser.getId()));
    }

    // ==================== SHIFT CHANGE REQUESTS ====================
    @GetMapping("/shift-changes/pending")
    public ResponseEntity<List<ShiftChangeResponse>> getPendingShiftChangeRequests(@PathVariable Long departmentId)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getPendingShiftChangeRequestsByDepartment(departmentId));
    }

    @GetMapping("/shift-changes")
    public ResponseEntity<List<ShiftChangeResponse>> getAllShiftChangeRequests(@PathVariable Long departmentId)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getShiftChangeRequestsByDepartment(departmentId));
    }

    @PutMapping("/shift-changes/{requestId}/approve")
    public ResponseEntity<ShiftChangeResponse> approveShiftChangeRequest(@PathVariable Long requestId,
                                                                         @Valid @RequestBody UpdateShiftChangeRequest request)
    {
        request.setStatus(Status.APPROVED);
        return ResponseEntity.ok(shiftChangeRequestService.updateShiftChangeRequestStatus(requestId, request));
    }

    @PutMapping("/shift-changes/{requestId}/reject")
    public ResponseEntity<ShiftChangeResponse> rejectShiftChangeRequest(@PathVariable Long requestId,
                                                                        @Valid @RequestBody UpdateShiftChangeRequest request)
    {
        request.setStatus(Status.REJECTED);
        return ResponseEntity.ok(shiftChangeRequestService.updateShiftChangeRequestStatus(requestId, request));
    }
}