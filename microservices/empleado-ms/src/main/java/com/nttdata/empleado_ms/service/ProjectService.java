package com.nttdata.empleado_ms.service;

import com.nttdata.empleado_ms.model.dto.ProjectRequestDTO;
import com.nttdata.empleado_ms.model.dto.ProjectResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectResponseDTO create(ProjectRequestDTO dto);
    List<ProjectResponseDTO> findAll();
    ProjectResponseDTO findById(Long id);
}
