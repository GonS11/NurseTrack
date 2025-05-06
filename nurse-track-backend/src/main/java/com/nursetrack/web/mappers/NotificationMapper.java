package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.Notification;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.notification.CreateNotificationRequest;
import com.nursetrack.web.dto.response.NotificationResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper
{
    @Mapping(target = "notificationType", source = "type") // Opcional: enviar el enum o type.name()
    NotificationResponse toDto(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Se asigna manualmente en el servicio
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", constant = "false")
    Notification toEntity(CreateNotificationRequest dto);

    @AfterMapping
    default void resolveCreateRelations(CreateNotificationRequest request,
                                        @MappingTarget Notification notification,
                                        @Context UserRepository userRepository)
    {
        notification.setUser(userRepository.getReferenceById(request.getUserId()));
    }

    List<NotificationResponse> toDtoList(List<Notification> entities);
}