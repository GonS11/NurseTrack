package com.nurse_track_back.nurse_track_back.domain.model;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column( nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ADMIN', 'SUPERVISOR', 'NURSE') DEFAULT 'NURSE'")
    private Role role;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    // Relación uno a uno con SupervisorDepartment
    @OneToOne(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private SupervisorDepartment supervisorDepartment;

    // Relación uno a muchos con NurseDepartment
    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NurseDepartment> nurseDepartments = new ArrayList<>();

    // Relación uno a muchos con Shift (turnos asignados)
    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> assignedShifts = new ArrayList<>();

    // Relación uno a muchos con Shift (turnos creados)
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> createdShifts = new ArrayList<>();

    // Relación uno a muchos con ShiftChangeRequest (solicitudes de cambio de turno realizadas)
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> shiftChangeRequests = new ArrayList<>();

    // Relación uno a muchos con ShiftChangeRequest (solicitudes de cambio de turno recibidas)
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> receivedShiftChangeRequests = new ArrayList<>();

    // Relación uno a muchos con ShiftChangeRequest (solicitudes de cambio de turno revisadas)
    @OneToMany(mappedBy = "reviewedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftChangeRequest> reviewedShiftChangeRequests = new ArrayList<>();

    // Relación uno a muchos con VacationRequest (solicitudes de vacaciones)
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> vacationRequests = new ArrayList<>();

    // Relación uno a muchos con VacationRequest (solicitudes de vacaciones revisadas)
    @OneToMany(mappedBy = "reviewedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> reviewedVacationRequests = new ArrayList<>();

    // Relación uno a muchos con Notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return isActive; }
}