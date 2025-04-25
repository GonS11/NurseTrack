package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.ShiftType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "shift_templates")
@Getter
@Setter
@NoArgsConstructor
public class ShiftTemplate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_time")
    private LocalTime shiftStartTime;

    @Column(name = "end_time")
    private LocalTime shiftEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type",
            columnDefinition = "ENUM('MORNING', 'AFTERNOON', 'NIGHT', 'HALF_MORNING', 'HALF_NIGHT')")
    private ShiftType shiftType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}