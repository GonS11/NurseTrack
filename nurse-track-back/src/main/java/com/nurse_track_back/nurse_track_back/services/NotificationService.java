package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.Notification;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.NotificationRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.notification.CreateNotificationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.NotificationResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getAllUserNotifications(Long id, int page, int size, String sortBy) {
        userRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.create("User", id));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Notification> notificationsPage = notificationRepository.findByUser_Id(id, pageable);

        return notificationsPage.map(notificationMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotification(Long id, Long userId) {
        return notificationRepository.findByIdAndUser_Id(id, userId)
                .map(notificationMapper::toDTO)
                .orElseThrow(() -> ResourceNotFoundException.create("Notification", id));
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request, userRepository);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    @Transactional
    public void markAsRead(Long id, Long userId) {
        // Antes tenia UpdateNotificationRequest pero innecesario
        notificationRepository.updateReadStatus(id, userId, true);
    }

    @Transactional
    public void deleteNotification(Long id, Long userId) {
        if (!notificationRepository.existsByIdAndUser_Id(id, userId))
            throw ResourceNotFoundException.create("Notification", id);

        notificationRepository.deleteById(id);
    }
}