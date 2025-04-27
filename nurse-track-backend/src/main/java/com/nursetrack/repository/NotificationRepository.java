package com.nursetrack.repository;

import com.nursetrack.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long id);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = :isRead WHERE n.id = :id AND n.user.id = :userId")
    void updateReadStatus(@Param("id") Long id,
                          @Param("userId") Long userId,
                          @Param("isRead") Boolean isRead);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    long countUnreadByUserId(@Param("userId") Long id);

    Boolean existsByIdAndUserId(Long id, Long userId);
}