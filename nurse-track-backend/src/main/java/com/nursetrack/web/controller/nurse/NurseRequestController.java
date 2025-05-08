package com.nursetrack.web.controller.nurse;

import com.nursetrack.domain.model.User;
import com.nursetrack.service.ShiftChangeRequestService;
import com.nursetrack.service.VacationRequestService;
import com.nursetrack.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nursetrack.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nursetrack.web.dto.response.ShiftChangeResponse;
import com.nursetrack.web.dto.response.VacationRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses/{nurseId}/requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('NURSE') and #nurseId == principal.id")
public class NurseRequestController
{
    private final VacationRequestService vacationRequestService;
    private final ShiftChangeRequestService shiftChangeRequestService;

    // ==================== VACATION REQUESTS ====================
    @GetMapping("/vacations/{requestId}")
    public ResponseEntity<VacationRequestResponse> getVacationRequestById(@PathVariable Long requestId,
                                                                          @AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(vacationRequestService.getVacationRequestById(requestId, currentUser.getId()));
    }

    @GetMapping("/vacations")
    public ResponseEntity<List<VacationRequestResponse>> getMyVacationRequests(@AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(vacationRequestService.getVacationRequestsByNurse(currentUser.getId()));
    }

    @PostMapping("/vacations")
    public ResponseEntity<VacationRequestResponse> createVacationRequest(@Valid @RequestBody CreateVacationRequest request,
                                                                         @AuthenticationPrincipal User currentUser)
    {
        request.setRequestingNurseId(currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(vacationRequestService.createVacationRequest(request));
    }

    // ==================== SHIFT CHANGE REQUESTS ====================
    @GetMapping("/shift-changes/{requestId}")
    public ResponseEntity<ShiftChangeResponse> getShiftChangeRequestById(@PathVariable Long requestId,
                                                                         @AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getShiftChangeRequestById(requestId, currentUser.getId()));
    }

    @GetMapping("/shift-changes")
    public ResponseEntity<List<ShiftChangeResponse>> getMyShiftChangeRequests(@AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getShiftChangeRequestsByNurse(currentUser.getId()));
    }

    @GetMapping("/shift-changes/received")
    public ResponseEntity<List<ShiftChangeResponse>> getReceivedShiftChangeRequests(@AuthenticationPrincipal User currentUser)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getReceivedShiftChangeRequests(currentUser.getId()));
    }

    @PostMapping("/shift-changes")
    public ResponseEntity<ShiftChangeResponse> createShiftChangeRequest(@Valid @RequestBody CreateShiftChangeRequest request,
                                                                        @AuthenticationPrincipal User currentUser)
    {
        request.setRequestingNurseId(currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftChangeRequestService.createShiftChangeRequest(request));
    }
}
