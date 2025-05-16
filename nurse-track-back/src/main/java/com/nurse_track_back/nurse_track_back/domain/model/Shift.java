package com.nurse_track_back.nurse_track_back.domain.model;

import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shifts", indexes = {
        @Index(name = "idx_shifts_coverage", columnList = "department_id, shift_date, status"),
        @Index(name = "idx_shifts_nurse_date", columnList = "nurse_id, shift_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "shift_template_id", nullable = false)
    private ShiftTemplate shiftTemplate;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'SWAPPED') DEFAULT 'SCHEDULED'")
    private ShiftStatus status;

    @Lob
    private String notes;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Relaciones con FK
    @OneToMany(mappedBy = "offeredShift")
    private List<ShiftChangeRequest> offeredInShiftChanges = new ArrayList<>();

    @OneToMany(mappedBy = "requestedShift")
    private List<ShiftChangeRequest> requestedInShiftChanges = new ArrayList<>();

}