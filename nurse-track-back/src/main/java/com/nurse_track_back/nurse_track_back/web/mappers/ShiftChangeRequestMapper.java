package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.ShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftChangeResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ShiftMapper.class, GenericUtilsMapper.class})
public interface ShiftChangeRequestMapper
{
    @Mapping(target = "requestingNurse", source = "requestingNurse", qualifiedByName = "userToSimpleResponse")
    @Mapping(target = "receivingNurse", source = "receivingNurse", qualifiedByName = "userToSimpleResponse")
    @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "userToSimpleResponse")
    @Mapping(target = "offeredShiftId", source = "offeredShift.id")
    @Mapping(target = "desiredShiftId", source = "desiredShift.id")
    ShiftChangeResponse toDTO(ShiftChangeRequest entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestingNurse", source = "requestingNurseId", qualifiedByName = "userIdToUser")
    @Mapping(target = "offeredShift", source = "offeredShiftId", qualifiedByName = "shiftIdToShift")
    @Mapping(target = "receivingNurse", source = "receivingNurseId", qualifiedByName = "userIdToUser")
    @Mapping(target = "desiredShift", source = "desiredShiftId", qualifiedByName = "shiftIdToShift")
    @Mapping(target = "reviewedNotes", ignore = true)
    @Mapping(target = "isInterchange", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ShiftChangeRequest toEntity(CreateShiftChangeRequest dto,
                                @Context UserRepository userRepository,
                                @Context ShiftRepository shiftRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestingNurse", ignore = true)
    @Mapping(target = "offeredShift", ignore = true)
    @Mapping(target = "receivingNurse", ignore = true)
    @Mapping(target = "desiredShift", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "isInterchange", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateModel(UpdateShiftChangeRequest request, @MappingTarget ShiftChangeRequest shiftChangeRequest);


    List<ShiftChangeResponse> toDTOList(List<ShiftChangeRequest> requests);
}