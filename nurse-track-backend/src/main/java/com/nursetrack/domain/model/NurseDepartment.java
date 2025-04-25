package com.nursetrack.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nurses_departments",
        indexes = {
                @Index(name = "idx_nurses_departments", columnList = "department_id, nurse_id"),
                @Index(name = "idx_nurse_assignments", columnList = "nurse_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NurseDepartment
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

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}