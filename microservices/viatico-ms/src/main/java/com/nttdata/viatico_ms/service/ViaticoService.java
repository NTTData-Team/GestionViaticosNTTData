package com.nttdata.viatico_ms.service;

import com.nttdata.viatico_ms.model.dto.ViaticoCreateDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoCreateWithGastosDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoResponseDTO;

public interface ViaticoService {
    ViaticoResponseDTO get(Long id);
    ViaticoResponseDTO updateEstado(Long id, String nuevoEstado);
    ViaticoResponseDTO create(ViaticoCreateWithGastosDTO dto);
}
