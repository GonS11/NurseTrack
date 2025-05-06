package com.nursetrack.web.controller.supervisor;

import com.nursetrack.domain.model.User;
import com.nursetrack.service.DepartmentService;
import com.nursetrack.service.NurseDepartmentService;
import com.nursetrack.service.ShiftService;
import com.nursetrack.service.SupervisorDepartmentService;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.request.shift.CreateShiftRequest;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import com.nursetrack.web.dto.response.ShiftResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/supervisor/departments/{departmentId}/requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERVISOR')")
public class SupervisorRequestController 
{
  //Acciones de ver todas las peticiones realizadas, accion de aceptar/rechazar alguna peticion
}
