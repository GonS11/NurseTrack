package com.nursetrack.domain;

import com.nursetrack.constants.AppEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts",
        indexes = {
                @Index(name = "idx_shifts_coverage", columnList = "department_id, shift_date, status"),
                @Index(name = "idx_shifts_nurse_date", columnList = "nurse_user_id, shift_date")
        })
@Getter
@Setter
@NoArgsConstructor
public class Shift
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_user_id", nullable = false)
    private User nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_template_id", nullable = false)
    private ShiftTemplate shiftTemplate;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(nullable = false)
    private LocalDateTime shiftStart;

    @Column(nullable = false)
    private LocalDateTime shiftEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.Status status = AppEnums.Status.SCHEDULED;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
