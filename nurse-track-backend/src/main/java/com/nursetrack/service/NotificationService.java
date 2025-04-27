package com.nursetrack.service;

import com.nursetrack.domain.model.Notification;
import com.nursetrack.exception.NotificationNotFoundException;
import com.nursetrack.repository.NotificationRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.notification.CreateNotificationRequest;
import com.nursetrack.web.dto.response.NotificationResponse;
import com.nursetrack.web.mappers.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService
{
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId)
    {
        return notificationMapper.toDtoList(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotification(Long id, Long userId)
    {
        return notificationRepository.findByIdAndUserId(id, userId)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request)
    {
        Notification notification = notificationMapper.toEntity(request);

        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    @Transactional
    public void markAsRead(Long id, Long userId)
    {
        //Antes tenia UpdateNotificationRequest pero innecesario
        notificationRepository.updateReadStatus(id, userId, true);
    }

    @Transactional
    public void deleteNotification(Long id, Long userId)
    {
        if (!notificationRepository.existsByIdAndUserId(id, userId)) throw new NotificationNotFoundException(id);

        notificationRepository.deleteById(id);
    }
}