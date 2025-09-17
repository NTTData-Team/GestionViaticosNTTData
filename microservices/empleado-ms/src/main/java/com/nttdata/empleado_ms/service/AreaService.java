package com.nttdata.empleado_ms.service;

import com.nttdata.empleado_ms.model.dto.AreaRequestDTO;
import com.nttdata.empleado_ms.model.dto.AreaResponseDTO;

import java.util.List;

public interface AreaService {
    AreaResponseDTO create(AreaRequestDTO dto);
    List<AreaResponseDTO> findAll();
    AreaResponseDTO findById(Long id);
}
