package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
        indexes = @Index(name = "idx_users_role_active", columnList = "role, is_active"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ADMIN', 'SUPERVISOR', 'NURSE')")
    private UserRole role = UserRole.NURSE;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "is_active")
    private Boolean isActive = true;

    //Authentication
    @Column(name = "jwt_token")
    private String jwtToken;

    //Authentication
    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToOne(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private SupervisorDepartment supervisorAssign;

    @OneToMany(mappedBy = "nurse", fetch = FetchType.LAZY)
    private List<NurseDepartment> nurseAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "nurse", fetch = FetchType.LAZY)
    private List<Shift> assignedShifts = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Shift> createdShifts = new ArrayList<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private List<ShiftChangeRequest> requestedShiftChanges = new ArrayList<>();

    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY)
    private List<ShiftChangeRequest> reviewedShiftChanges = new ArrayList<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private List<VacationRequest> vacationRequests = new ArrayList<>();

    @OneToMany(mappedBy = "reviewedBy", fetch = FetchType.LAZY)
    private List<VacationRequest> reviewedVacationRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}