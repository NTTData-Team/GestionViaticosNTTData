package com.nttdata.gasto_ms.service;

import com.nttdata.gasto_ms.model.dto.GastoCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoResponseDTO;
import com.nttdata.gasto_ms.model.dto.GastoUpdateDTO;

import java.util.List;

public interface GastoService {
    GastoResponseDTO create(GastoCreateDTO dto);
    List<GastoResponseDTO> listByViatico(Long viaticoId);
    GastoResponseDTO get(Long id);
    GastoResponseDTO update(Long id, GastoUpdateDTO dto); // <-- cambia a UpdateDTO
    void delete(Long id);
}
