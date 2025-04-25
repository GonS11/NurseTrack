package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.User;
import com.nursetrack.web.dto.request.user.CreateUserRequest;
import com.nursetrack.web.dto.request.user.UpdateUserRequest;
import com.nursetrack.web.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    @Mapping(source = "role.displayName", target = "roleDisplayName")
    @Mapping(source = "role.description", target = "roleDescription")
    @Mapping(expression = "java(user.getFullName())", target = "fullName")
    UserResponse toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "jwtToken", ignore = true)
    @Mapping(target = "tokenExpiry", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "supervisorAssign", ignore = true)
    @Mapping(target = "nurseAssignments", ignore = true)
    @Mapping(target = "assignedShifts", ignore = true)
    User toEntity(CreateUserRequest dto);
    
    void updateEntity(UpdateUserRequest dto, @MappingTarget User entity);

    List<UserResponse> toDtoList(List<User> all);
}