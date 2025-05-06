package com.nursetrack.repository;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* [action][By][property1][operation1][And][property2][operation2]...() */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Búsquedas únicas
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username,
                                         @Param("email") String email);

    Optional<User> findByLicenseNumber(String licenseNumber);

    // Búsquedas paginadas
    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByIsActive(Boolean active, Pageable pageable);
    Page<User> findByIsActiveAndRole(Boolean active, UserRole role, Pageable pageable);

    // Búsqueda flexible
    @Query("""
        SELECT u FROM User u 
        WHERE (:query IS NULL OR 
              LOWER(u.username) LIKE %:query% OR 
              LOWER(u.email) LIKE %:query% OR
              LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE %:query%)
        AND (:role IS NULL OR u.role = :role)
        AND (:active IS NULL OR u.isActive = :active)
        """)
    Page<User> searchUsers(@Param("query") String query,
                           @Param("role") UserRole role,
                           @Param("active") Boolean active,
                           Pageable pageable);

    // Validaciones
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByLicenseNumber(String licenseNumber);
}