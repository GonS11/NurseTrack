package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shift_change_requests", indexes = {
        @Index(name = "idx_shift_change_requester", columnList = "requester_id, status"),
        @Index(name = "idx_shift_change_timestamps", columnList = "created_at, reviewed_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftChangeRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requestingNurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offered_shift_id", nullable = false)
    private Shift offeredShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User receivingNurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_shift_id", nullable = false)
    private Shift desiredShift;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Lob
    @Column(name = "reviewed_notes", columnDefinition = "TEXT")
    private String reviewedNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            columnDefinition = "ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')")
    private Status status = Status.PENDING;

    @Column(name = "is_interchange", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isInterchange = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}