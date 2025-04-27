package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.User;
import com.nursetrack.web.dto.response.CurrentUserResponse;
import com.nursetrack.web.dto.response.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper
{
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "tokenType", constant = "Bearer")
    LoginResponse toLoginResponse(User user, String token);

    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    CurrentUserResponse toCurrentUserResponse(User user);
}