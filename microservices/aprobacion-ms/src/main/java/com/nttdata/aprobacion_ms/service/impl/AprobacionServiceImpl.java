package com.nttdata.aprobacion_ms.service.impl;

import com.nttdata.aprobacion_ms.exception.AprobacionNotFoundException;
import com.nttdata.aprobacion_ms.exception.InvalidDecisionException;
import com.nttdata.aprobacion_ms.model.dto.*;
import com.nttdata.aprobacion_ms.model.entity.AprobacionEntity;
import com.nttdata.aprobacion_ms.model.mapper.AprobacionMapper;
import com.nttdata.aprobacion_ms.repository.AprobacionRepository;
import com.nttdata.aprobacion_ms.service.AprobacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AprobacionServiceImpl implements AprobacionService {

    private final AprobacionRepository repo;

    @Override
    @Transactional
    public AprobacionResponseDTO create(AprobacionCreateDTO dto) {
        log.debug("Creando aprobación viaticoId={} nivel={}", dto.viaticoId(), dto.nivel());
        var entity = AprobacionMapper.toEntity(dto, new AprobacionEntity());
        return AprobacionMapper.toDto(repo.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public AprobacionResponseDTO get(Long id) {
        return repo.findById(id).map(AprobacionMapper::toDto)
                .orElseThrow(() -> new AprobacionNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AprobacionResponseDTO> listByViatico(Long viaticoId) {
        return repo.findByViaticoId(viaticoId).stream().map(AprobacionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AprobacionResponseDTO> listPendientesByAprobador(Long aprobadorId) {
        return repo.findByEstadoAndAprobadorId(AprobacionEntity.Estado.PENDIENTE, aprobadorId)
                .stream().map(AprobacionMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AprobacionResponseDTO approve(Long id, AprobacionDecisionDTO dto) {
        var e = repo.findById(id).orElseThrow(() -> new AprobacionNotFoundException(id));
        if (e.getEstado() != AprobacionEntity.Estado.PENDIENTE) {
            throw new InvalidDecisionException("Solo se puede aprobar cuando el estado es PENDIENTE");
        }
        e.setEstado(AprobacionEntity.Estado.APROBADO);
        e.setAprobadorId(dto.aprobadorId());
        e.setComentario(dto.comentario());
        e.setDecididoEn(OffsetDateTime.now());
        return AprobacionMapper.toDto(repo.save(e));
    }

    @Override
    @Transactional
    public AprobacionResponseDTO reject(Long id, AprobacionDecisionDTO dto) {
        var e = repo.findById(id).orElseThrow(() -> new AprobacionNotFoundException(id));
        if (e.getEstado() != AprobacionEntity.Estado.PENDIENTE) {
            throw new InvalidDecisionException("Solo se puede rechazar cuando el estado es PENDIENTE");
        }
        e.setEstado(AprobacionEntity.Estado.RECHAZADO);
        e.setAprobadorId(dto.aprobadorId());
        e.setComentario(dto.comentario());
        e.setDecididoEn(OffsetDateTime.now());
        return AprobacionMapper.toDto(repo.save(e));
    }
}
