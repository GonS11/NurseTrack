package com.nurse_track_back.nurse_track_back.domain.models;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ROLE_ADMIN','ROLE_SUPERVISOR','ROLE_NURSE') DEFAULT 'ROLE_NURSE'")
    private Role role = Role.ROLE_NURSE;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SupervisorDepartment> supervisorDepartments;

    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NurseDepartment> nurseDepartments;

    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> assignedShifts;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> createdShifts;

    @OneToMany(mappedBy = "requestingNurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> shiftChangeRequests;

    @OneToMany(mappedBy = "receivingNurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> receivedShiftChangeRequests;

    @OneToMany(mappedBy = "reviewedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> reviewedShiftChangeRequests;

    @OneToMany(mappedBy = "requestingNurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> vacationRequests;

    @OneToMany(mappedBy = "reviewedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> reviewedVacationRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    // UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}