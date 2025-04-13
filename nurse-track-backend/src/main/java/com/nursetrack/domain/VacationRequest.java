package com.nursetrack.domain;

import com.nursetrack.constants.AppEnums;
import jakarta.persistence.*;
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
                @Index(name = "idx_vacation_requester_status", columnList = "requester_user_id, status")
        },
        uniqueConstraints = @UniqueConstraint(name = "unq_vacation_user",columnNames = {"requester_user_id","start_date","end_date"}))
@Getter
@Setter
@NoArgsConstructor
public class VacationRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacation_request_id")
    private Long vacationRequestId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requestedBy;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Lob //Lob se usa para Large Objet como text, clob...
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.Status status = AppEnums.Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedBy;

    @Column()
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
