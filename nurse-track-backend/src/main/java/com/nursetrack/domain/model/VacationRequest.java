package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private User requestingNurse;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Lob
    @Column(name = "reviewed_notes", columnDefinition = "TEXT")
    private String reviewedNotes;

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
}
