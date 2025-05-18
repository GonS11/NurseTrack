package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.Notification;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.notification.CreateNotificationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.NotificationResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GenericUtilsMapper.class,UserMapper.class})
public interface NotificationMapper
{
    @Mapping(target = "user", source = "user", qualifiedByName = "userToSimpleResponse")
    NotificationResponse toDTO(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", constant = "false")
    Notification toEntity(CreateNotificationRequest request, @Context UserRepository userRepository);

    List<NotificationResponse> toDtoList(List<Notification> entities);
}
