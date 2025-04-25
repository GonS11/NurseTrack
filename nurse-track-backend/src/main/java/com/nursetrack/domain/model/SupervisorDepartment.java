package com.nursetrack.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "supervisors_departments",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_supervisor_department",
                columnNames = {"supervisor_id", "department_id"}),
        indexes = {
                @Index(name = "idx_supervisors_departments",
                        columnList = "department_id, supervisor_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorDepartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}
