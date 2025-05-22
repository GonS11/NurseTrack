package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.VacationRequest;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.UpdateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.VacationRequestResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserMapper.class, GenericUtilsMapper.class })
public interface VacationRequestMapper {
    @Mapping(target = "requestingNurse", source = "requestingNurse", qualifiedByName = "userToSimpleResponse")
    @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "userToSimpleResponse")
    VacationRequestResponse toDTO(VacationRequest entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestingNurse", source = "requestingNurseId", qualifiedByName = "userIdToUser")
    @Mapping(target = "reviewedBy", source = "reviewedById", qualifiedByName = "userIdToUser")
    @Mapping(target = "reviewedNotes", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    VacationRequest toEntity(CreateVacationRequest dto, @Context UserRepository userRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestingNurse", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateModel(UpdateVacationRequest request,
            @MappingTarget VacationRequest vacationRequest,
            @Context UserRepository userRepository);
}