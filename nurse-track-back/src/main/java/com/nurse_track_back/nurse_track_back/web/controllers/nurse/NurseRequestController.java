package com.nurse_track_back.nurse_track_back.web.controllers.nurse;

import com.nurse_track_back.nurse_track_back.services.ShiftChangeRequestService;
import com.nurse_track_back.nurse_track_back.services.VacationRequestService;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftChangeResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.VacationRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses/{nurseId}/requests")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('NURSE') and #nurseId == principal.id")
public class NurseRequestController
{
    private final VacationRequestService vacationRequestService;
    private final ShiftChangeRequestService shiftChangeRequestService;

    // ==================== VACATION REQUESTS ====================
    @GetMapping("/vacations")
    public ResponseEntity<List<VacationRequestResponse>> getMyVacationRequests(@PathVariable Long nurseId)
    {
        return ResponseEntity.ok(vacationRequestService.getVacationRequestsByNurse(nurseId));
    }

    @GetMapping("/vacations/{requestId}")
    public ResponseEntity<VacationRequestResponse> getVacationRequestById(@PathVariable Long requestId,
                                                                          @PathVariable Long nurseId)
    {
        return ResponseEntity.ok(vacationRequestService.getVacationRequestById(requestId, nurseId));
    }

    @PostMapping("/vacations")
    public ResponseEntity<VacationRequestResponse> createVacationRequest(@Valid @RequestBody CreateVacationRequest request,
                                                                         @PathVariable Long nurseId)
    {
        request.setRequestingNurseId(nurseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(vacationRequestService.createVacationRequest(request));
    }

    // ==================== SHIFT CHANGE REQUESTS ====================
    @GetMapping("/shift-changes")
    public ResponseEntity<List<ShiftChangeResponse>> getMyShiftChangeRequests(@PathVariable Long nurseId)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getShiftChangeRequestsByNurse(nurseId));
    }

    @GetMapping("/shift-changes/{requestId}")
    public ResponseEntity<ShiftChangeResponse> getShiftChangeRequestById(@PathVariable Long requestId,
                                                                         @PathVariable Long nurseId)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getShiftChangeRequestById(requestId, nurseId));
    }

    @GetMapping("/shift-changes/received")
    public ResponseEntity<List<ShiftChangeResponse>> getReceivedShiftChangeRequests(@PathVariable Long nurseId)
    {
        return ResponseEntity.ok(shiftChangeRequestService.getReceivedShiftChangeRequests(nurseId));
    }

    @PostMapping("/shift-changes")
    public ResponseEntity<ShiftChangeResponse> createShiftChangeRequest(@Valid @RequestBody CreateShiftChangeRequest request,
                                                                        @PathVariable Long nurseId)
    {
        request.setRequestingNurseId(nurseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftChangeRequestService.createShiftChangeRequest(request));
    }
}
