package com.nursetrack.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //FK
    @OneToOne(mappedBy = "department")
    private SupervisorDepartment supervisorAssignment;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<NurseDepartment> nurseAssignment = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Shift> shifts = new ArrayList<>();
}