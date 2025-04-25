package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts", indexes = {
        @Index(name = "idx_shifts_coverage", columnList = "department_id, shift_date, status"),
        @Index(name = "idx_shifts_nurse_date", columnList = "nurse_id, shift_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shift
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private User nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_template_id")
    private ShiftTemplate shiftTemplate;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            columnDefinition = "ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED')")
    private Status status = Status.SCHEDULED;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Relaciones con FK
    @OneToOne(mappedBy = "requestedShift", fetch = FetchType.LAZY)
    private ShiftChangeRequest requestedInShiftChange;

    @OneToOne(mappedBy = "offeredShift", fetch = FetchType.LAZY)
    private ShiftChangeRequest offeredInShiftChange;


}