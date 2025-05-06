package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.VacationRequest;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nursetrack.web.dto.request.vacationRequest.UpdateVacationRequest;
import com.nursetrack.web.dto.response.VacationRequestResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface VacationRequestMapper
{
    @Mapping(source = "requestingNurse", target = "requester", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "reviewedBy", target = "reviewedBy", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "reviewedNotes", target = "reviewedNotes")
    VacationRequestResponse toDTO(VacationRequest entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    VacationRequest toEntity(CreateVacationRequest dto);

    @AfterMapping
    default void resolveCreateRelations(CreateVacationRequest request,
                                        @MappingTarget VacationRequest vacationRequest,
                                        @Context UserRepository userRepository)
    {
        vacationRequest.setRequestingNurse(userRepository.getReferenceById(request.getRequestingNurseId()));
    }

    @Mapping(target = "requestingNurse", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateModel(UpdateVacationRequest request,
                     @MappingTarget VacationRequest vacationRequest,
                     @Context UserRepository userRepository);
}