package com.nttdata.empleado_ms.controller;

import com.nttdata.empleado_ms.model.dto.ProjectRequestDTO;
import com.nttdata.empleado_ms.model.dto.ProjectResponseDTO;
import com.nttdata.empleado_ms.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponseDTO create(@Valid @RequestBody ProjectRequestDTO dto) {
        return projectService.create(dto);
    }

    @GetMapping
    public List<ProjectResponseDTO> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

}
