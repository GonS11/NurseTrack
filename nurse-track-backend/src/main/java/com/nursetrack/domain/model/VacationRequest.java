package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacation_requests",
        indexes = {
                @Index(name = "idx_vacation_dates", columnList = "start_date, end_date"),
                @Index(name = "idx_vacation_requester_status", columnList = "requester_id, status")
        },
        uniqueConstraints = @UniqueConstraint(
                name = "unq_vacation_user_dates",
                columnNames = {"requester_id", "start_date", "end_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacationRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')")
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @AssertTrue(message = "End date must be after or equal to start date")
    public boolean isDateRangeValid() {
        return endDate == null || startDate == null || !endDate.isBefore(startDate);
    }

    @PrePersist
    @PreUpdate
    private void validateRequest()
    {
        // Validar que si está aprobado/rechazado, tenga revisor
        if ((status == Status.APPROVED || status == Status.REJECTED) && reviewedBy == null)
        {
            throw new IllegalStateException("Reviewed by must be set when status is APPROVED or REJECTED");
        }

        // Validar rango de fechas
        if (endDate.isBefore(startDate))
        {
            throw new IllegalStateException("End date cannot be before start date");
        }
    }
}
