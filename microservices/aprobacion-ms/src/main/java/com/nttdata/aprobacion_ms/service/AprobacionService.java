package com.nttdata.aprobacion_ms.service;

import com.nttdata.aprobacion_ms.model.dto.*;

import java.util.List;

public interface AprobacionService {
    AprobacionResponseDTO create(AprobacionCreateDTO dto);
    AprobacionResponseDTO get(Long id);
    List<AprobacionResponseDTO> listByViatico(Long viaticoId);
    List<AprobacionResponseDTO> listPendientesByAprobador(Long aprobadorId);
    AprobacionResponseDTO approve(Long id, AprobacionDecisionDTO dto);
    AprobacionResponseDTO reject(Long id, AprobacionDecisionDTO dto);
}
