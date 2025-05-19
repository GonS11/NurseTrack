package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.CreateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.UpdateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.UserResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.UserSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Entity -> Response
    @Named("userToDto")
    UserResponse toDTO(User user);

    @Named("userToSimpleResponse")
    UserSimpleResponse toUserSimpleResponse(User user);

    // CreateRequest -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "supervisorDepartment", ignore = true)
    @Mapping(target = "nurseDepartments", ignore = true)
    @Mapping(target = "assignedShifts", ignore = true)
    @Mapping(target = "createdShifts", ignore = true)
    @Mapping(target = "shiftChangeRequests", ignore = true)
    @Mapping(target = "receivedShiftChangeRequests", ignore = true)
    @Mapping(target = "reviewedShiftChangeRequests", ignore = true)
    @Mapping(target = "vacationRequests", ignore = true)
    @Mapping(target = "reviewedVacationRequests", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    User toEntity(CreateUserRequest request);

    // UpdateRequest -> Entity existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "supervisorDepartment", ignore = true)
    @Mapping(target = "nurseDepartments", ignore = true)
    @Mapping(target = "assignedShifts", ignore = true)
    @Mapping(target = "createdShifts", ignore = true)
    @Mapping(target = "shiftChangeRequests", ignore = true)
    @Mapping(target = "receivedShiftChangeRequests", ignore = true)
    @Mapping(target = "reviewedShiftChangeRequests", ignore = true)
    @Mapping(target = "vacationRequests", ignore = true)
    @Mapping(target = "reviewedVacationRequests", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateModel(UpdateUserRequest request, @MappingTarget User user);

    // List
    List<UserResponse> toDTOList(List<User> users);

}
