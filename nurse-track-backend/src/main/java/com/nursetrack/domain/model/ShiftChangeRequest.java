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
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_shift_id")
    private Shift requestedShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offered_shift_id")
    private Shift offeredShift;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            columnDefinition = "ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')")
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //Mandar al servicio?
    @PrePersist
    @PreUpdate
    private void validateRequest()
    {
        if (requester == null || requestedShift == null)
        {
            throw new IllegalStateException("Requester and requested shift are mandatory");
        }

        if (offeredShift != null)
        {
            if (offeredShift.equals(requestedShift))
            {
                throw new IllegalStateException("Requested and offered shifts cannot be the same");
            }
            if (!offeredShift.getNurse().equals(requester))
            {
                throw new IllegalStateException("Offered shift must belong to the requester");
            }
        }
    }
}