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
        indexes = {
                @Index(name = "idx_shift_templates_department", columnList = "department_id"),
                @Index(name = "idx_shift_templates_creator", columnList = "created_by_user_id")
        })
@Getter
@Setter
@NoArgsConstructor
public class ShiftTemplate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_template_id")
    private Long shiftTemplateId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "shift_start_time", nullable = false, columnDefinition = "TIME")
    private Time shiftStartTime;

    @Column(name = "shift_end_time", nullable = false, columnDefinition = "TIME")
    private Time shiftEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.ShiftTypes shiftType;

    @Column(name = "display_color", length = 7)
    private String displayColor = "#3498db";

    //Diferencias FetchType, carga toda la info relacionada siempre o no
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private Boolean createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
