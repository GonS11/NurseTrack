package com.nurse_track_back.nurse_track_back.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "supervisors_departments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supervisor_id", "department_id"}),
        indexes = @Index(name = "idx_supervisors_departments", columnList = "department_id, supervisor_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupervisorDepartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @OneToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}