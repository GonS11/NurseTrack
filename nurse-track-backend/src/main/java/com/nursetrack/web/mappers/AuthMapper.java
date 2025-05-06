package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.User;
import com.nursetrack.web.dto.response.CurrentUserResponse;
import com.nursetrack.web.dto.response.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "token", source = "token")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "licenseNumber", source = "user.licenseNumber")
    LoginResponse toLoginResponse(User user, String token);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "licenseNumber", source = "licenseNumber")
    @Mapping(target = "isActive", source = "isActive")
    CurrentUserResponse toCurrentUserResponse(User user);
}