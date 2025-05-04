package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftChangeResponse
{
    private Long id;
    private UserSimpleResponse requestingNurse;
    private Long offeredShiftId;
    private UserSimpleResponse receivingNurse;
    private Long desiredShiftId;
    private String reason; // Motivo de la solicitud
    private String reviewedNotes;   // Comentarios del revisor
    private Status status;
    private UserSimpleResponse reviewedBy;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}