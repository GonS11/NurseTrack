package com.nurse_track_back.nurse_track_back.web.dto.response;


import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
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
    private RequestStatus status;
    private UserSimpleResponse reviewedBy;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}
