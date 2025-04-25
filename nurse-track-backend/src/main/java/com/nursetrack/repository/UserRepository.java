package com.nursetrack.repository;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* [action][By][property1][operation1][And][property2][operation2]...() */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByLicenseNumber(String licenseNumber);
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
    Optional<User> findByUsernameOrEmail(@Param("username") String username);

    List<User> findByRole(UserRole role);
    List<User> findByIsActive(boolean isActive);
    List<User> findByRoleAndIsActive(UserRole role, boolean isActive);

    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByIsActive(boolean isActive, Pageable pageable);
    Page<User> findByRoleAndIsActive(UserRole role, boolean isActive, Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByLicenseNumber(String licenseNumber);

    Optional<User> findByIdAndRole(Long id, UserRole role);
}