package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    Page<Notification> findByUserId(Long userId, Pageable pageable);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = :isRead WHERE n.id = :id AND n.user.id = :userId")
    void updateReadStatus(@Param("id") Long id,
                          @Param("userId") Long userId,
                          @Param("isRead") Boolean isRead);

    boolean existsByIdAndUserId(Long id, Long userId);
}
