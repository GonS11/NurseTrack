package com.nursetrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "supervisors_departments",
        indexes = @Index(name = "idx_supervisors_departments",columnList = "department_id, supervisor_user_id"),
        uniqueConstraints = @UniqueConstraint(name = "unq_supervisor_department",columnNames = {"supervisor_user_id","department_id"}))
@Getter
@Setter
@NoArgsConstructor
public class SupervisorDepartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supervisorDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_user_id", nullable = false)
    private User supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime assignedAt;
}
