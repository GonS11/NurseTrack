package com.nursetrack.domain;

import com.nursetrack.constants.AppEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift_templates",
        indexes = @Index(name = "idx_shift_templates_creator", columnList = "created_by_user_id"))
@Getter
@Setter
@NoArgsConstructor
public class ShiftTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftTemplateId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "TIME")
    private Time shiftStartTime;

    @Column(nullable = false, columnDefinition = "TIME")
    private Time shiftEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.ShiftTypes shiftType;

    @Column(length = 7)
    private String displayColor = "#3498db";

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
