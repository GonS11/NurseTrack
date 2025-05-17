package com.nurse_track_back.nurse_track_back.web.controllers.shared;

import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftTemplateResponse;
import com.nurse_track_back.nurse_track_back.services.ShiftTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shift-templates")
@RequiredArgsConstructor
public class ShiftTemplateController
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
        return ResponseEntity.ok(shiftTemplateService.getTemplateById(id));
    }
}
