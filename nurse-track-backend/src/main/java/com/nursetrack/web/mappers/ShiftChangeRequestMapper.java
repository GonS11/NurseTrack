package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.ShiftChangeRequest;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nursetrack.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import com.nursetrack.web.dto.response.ShiftChangeResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ShiftMapper.class})
public interface ShiftChangeRequestMapper
{
    @Mapping(target = "requestingNurse", source = "requestingNurse")
    @Mapping(target = "receivingNurse", source = "receivingNurse")
    @Mapping(target = "offeredShiftId", source = "offeredShift.id")
    @Mapping(target = "desiredShiftId", source = "desiredShift.id")
    ShiftChangeResponse toDTO(ShiftChangeRequest entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(Status.PENDING)")
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ShiftChangeRequest toEntity(CreateShiftChangeRequest dto);

    @AfterMapping
    default void resolveCreateRelations(CreateShiftChangeRequest request,
                                        @MappingTarget ShiftChangeRequest shiftChangeRequest,
                                        @Context UserRepository userRepository,
                                        @Context ShiftRepository shiftRepository)
    {
        //Asignaciones FK
        shiftChangeRequest.setRequestingNurse(userRepository.getReferenceById(request.getRequestingNurseId()));
        shiftChangeRequest.setOfferedShift(shiftRepository.getReferenceById(request.getOfferedShiftId()));
        shiftChangeRequest.setReceivingNurse(userRepository.getReferenceById(request.getReceivingNurseId()));
        shiftChangeRequest.setDesiredShift(shiftRepository.getReferenceById(request.getDesiredShiftId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestingNurse", ignore = true)
    @Mapping(target = "offeredShift", ignore = true)
    @Mapping(target = "receivingNurse", ignore = true)
    @Mapping(target = "desiredShift", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateModel(UpdateShiftChangeRequest request, @MappingTarget ShiftChangeRequest shiftChangeRequest);


    List<ShiftChangeResponse> toDTOList(List<ShiftChangeRequest> requests);
}