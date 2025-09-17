package com.nttdata.empleado_ms.service.impl;

import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.mapper.AreaMapper;
import com.nttdata.empleado_ms.model.dto.AreaRequestDTO;
import com.nttdata.empleado_ms.model.dto.AreaResponseDTO;
import com.nttdata.empleado_ms.model.entity.AreaEntity;
import com.nttdata.empleado_ms.repository.AreaRepository;
import com.nttdata.empleado_ms.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;

    @Override
    public AreaResponseDTO create(AreaRequestDTO dto) {
        if (areaRepository.existsByName(dto.getName()))
            throw new IllegalArgumentException("Ya existe una área con ese nombre");
        AreaEntity area = areaMapper.toEntity(dto);
        area = areaRepository.save(area);
        return areaMapper.toResponse(area);
    }

    @Override
    public List<AreaResponseDTO> findAll() {
        return areaMapper.toResponseList(areaRepository.findAll());
    }

    @Override
    public AreaResponseDTO findById(Long id) {
        AreaEntity area = areaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Area con id -> " + id + " no encontrado"));
        return areaMapper.toResponse(area);
    }
}
