package com.nurse_track_back.nurse_track_back.domain.models;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacation_requests", uniqueConstraints = @UniqueConstraint(columnNames = { "requester_id", "start_date",
                "end_date" }), indexes = {
                                @Index(name = "idx_vacation_dates", columnList = "start_date, end_date"),
                                @Index(name = "idx_vacation_requester_status", columnList = "requester_id, status")
                })
@SQLRestriction("end_date >= start_date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "requester_id", nullable = false)
        private User requestingNurse;

        @Column(name = "start_date", nullable = false)
        private LocalDate startDate;

        @Column(name = "end_date", nullable = false)
        private LocalDate endDate;

        @Lob
        private String reason;

        @Lob
        @Column(name = "reviewed_notes")
        private String reviewedNotes;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(columnDefinition = "ENUM('CANCELLED','PENDING','APPROVED','REJECTED') DEFAULT 'PENDING'")
        private RequestStatus status = RequestStatus.PENDING;

        @ManyToOne
        @JoinColumn(name = "reviewed_by_id")
        private User reviewedBy;

        @Column(name = "reviewed_at")
        private LocalDateTime reviewedAt;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;
}