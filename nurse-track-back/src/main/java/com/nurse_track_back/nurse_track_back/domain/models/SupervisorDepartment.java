package com.nurse_track_back.nurse_track_back.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "supervisors_departments",
        uniqueConstraints = @UniqueConstraint(columnNames = "department_id")) // Departamento único
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupervisorDepartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Un supervisor puede tener múltiples asignaciones
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @OneToOne  // Un departamento solo puede estar en una asignación
    @JoinColumn(name = "department_id", nullable = false, unique = true)
    private Department department;

    @CreationTimestamp
    private LocalDateTime assignedAt;
}