package com.nttdata.empleado_ms.service.impl;

import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.mapper.ProjectMapper;
import com.nttdata.empleado_ms.model.dto.ProjectRequestDTO;
import com.nttdata.empleado_ms.model.dto.ProjectResponseDTO;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import com.nttdata.empleado_ms.repository.ProjectRepository;
import com.nttdata.empleado_ms.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        if(projectRepository.existsByName(dto.getName()))
            throw new IllegalArgumentException("Ya existe un proyecto con ese nombre");
        ProjectEntity projectEntity = projectMapper.toEntity(dto);
        projectEntity = projectRepository.save(projectEntity);
        return projectMapper.toResponse(projectEntity);
    }

    @Override
    public List<ProjectResponseDTO> findAll() {
        return projectMapper.toResponseList(projectRepository.findAll());
    }

    @Override
    public ProjectResponseDTO findById(Long id) {
        ProjectEntity project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe el proyecto con el id: " + id));
        return projectMapper.toResponse(project);
    }
}
