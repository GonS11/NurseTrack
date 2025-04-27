package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.User;
import com.nursetrack.web.dto.request.user.CreateUserRequest;
import com.nursetrack.web.dto.request.user.UpdateUserRequest;
import com.nursetrack.web.dto.response.CurrentUserResponse;
import com.nursetrack.web.dto.response.UserResponse;
import com.nursetrack.web.dto.response.UserSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    // Mapeo desde CreateUserRequest a User (para creación)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toEntity(CreateUserRequest dto);

    // UserResponse (fullName se calcula automáticamente vía getter)
    @Mapping(target = "roleDisplayName", source = "role.displayName")
    @Mapping(target = "roleDescription", source = "role.description")
    UserResponse toDto(User user);  // Cambiado de toUserResponse a toDto para coincidir con el servicio

    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserSimpleResponse toUserSimpleResponse(User user);

    // CurrentUserResponse (hereda campos de UserResponse)
    @Mapping(target = "roleDisplayName", source = "role.displayName")
    @Mapping(target = "roleDescription", source = "role.description")
    CurrentUserResponse toCurrentUserResponse(User user);

    // Update/ignore fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget User user);

    List<UserResponse> toDtoList(List<User> users);
}