package com.nursetrack.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nurses_departments",
        indexes = @Index(name = "idx_nurses_departments",columnList = "department_id, nurse_user_id"))
@Getter
@Setter
@NoArgsConstructor
public class NurseDepartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nurseDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_user_id", nullable = false)
    private User nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime assignedAt;
}