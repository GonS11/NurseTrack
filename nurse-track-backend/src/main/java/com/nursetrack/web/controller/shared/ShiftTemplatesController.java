package com.nursetrack.web.controller.shared;

import com.nursetrack.service.ShiftTemplateService;
import com.nursetrack.web.dto.response.ShiftTemplateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shift-templates")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
public class ShiftTemplatesController
{
    private final ShiftTemplateService shiftTemplateService;

    @GetMapping
    public ResponseEntity<List<ShiftTemplateResponse>> getShiftTemplates()
    {
        return ResponseEntity.ok(shiftTemplateService.getAllTemplates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftTemplateResponse> getShiftTemplateById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(shiftTemplateService.getEntityById(id));
    }
}
