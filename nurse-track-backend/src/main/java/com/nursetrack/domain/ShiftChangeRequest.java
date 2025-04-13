package com.nursetrack.domain;

import com.nursetrack.constants.AppEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shift_change_requests",
        indexes = {
                @Index(name = "idx_shift_change_requester", columnList = "requester_user_id, status"),
                @Index(name = "idx_shift_change_timestamps", columnList = "created_at, reviewed_at")
        })
@Getter
@Setter
@NoArgsConstructor
public class ShiftChangeRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftChangeRequestId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requestedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_shift_id", nullable = false)
    private Shift requestedShift;

    @ManyToOne
    @JoinColumn(name = "offered_shift_id")
    private Shift offeredShift;

    @Lob //Lob se usa para Large Objet como text, clob...
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.Status status = AppEnums.Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}