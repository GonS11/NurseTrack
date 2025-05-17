package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.Notification;
import com.nurse_track_back.nurse_track_back.web.dto.request.notification.CreateNotificationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.NotificationResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface NotificationMapper
{
    @Mapping(target = "user", source = "user", qualifiedByName = "userToSimpleResponse") // Mapeo correcto para User
    @Mapping(target = "type", source = "type") // Eliminar qualifiedByName
    NotificationResponse toDTO(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", constant = "false")
    Notification toEntity(CreateNotificationRequest dto);

    List<NotificationResponse> toDtoList(List<Notification> entities);
}
