package com.nttdata.gasto_ms.service.impl;

import com.nttdata.gasto_ms.client.ViaticoClient;
import com.nttdata.gasto_ms.exception.BusinessRuleException;
import com.nttdata.gasto_ms.exception.GastoNotFoundException;
import com.nttdata.gasto_ms.model.dto.GastoCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoItemCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoResponseDTO;
import com.nttdata.gasto_ms.model.dto.GastoUpdateDTO;
import com.nttdata.gasto_ms.model.entity.GastoEntity;
import com.nttdata.gasto_ms.model.mapper.GastoMapper;
import com.nttdata.gasto_ms.repository.GastoRepository;
import com.nttdata.gasto_ms.service.GastoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {

    private final GastoRepository repo;
    private final ViaticoClient viaticoClient;
    @Override
    @Transactional
    public GastoResponseDTO create(GastoCreateDTO dto) {
        var viatico = viaticoClient.get(dto.viaticoId());
        if ("RECHAZADO".equalsIgnoreCase(viatico.estado()) || "APROBADO".equalsIgnoreCase(viatico.estado())) {
            throw new BusinessRuleException("No se pueden registrar gastos porque el viático está RECHAZADO U APROBADO");
        }
        if (dto.fecha().isBefore(viatico.fechaInicio()) || dto.fecha().isAfter(viatico.fechaFin())){
            throw new BusinessRuleException("La fecha del gasto está fuera del rango del viático");
        }
        log.debug("Creando gasto viaticoId={}", dto.viaticoId());
        var entity = GastoMapper.toEntity(dto, new GastoEntity());
        return GastoMapper.toDto(repo.save(entity));
    }
    @Override
    @Transactional
    public List<GastoResponseDTO> createBatch(Long viaticoId, List<GastoItemCreateDTO> items) {
        var viatico = viaticoClient.get(viaticoId);
        if ("RECHAZADO".equalsIgnoreCase(viatico.estado()))
            throw new BusinessRuleException("No se pueden registrar gastos porque el viático está RECHAZADO");

        var entities = items.stream().map(i -> {
            if (i.fecha().isBefore(viatico.fechaInicio()) || i.fecha().isAfter(viatico.fechaFin())){
                throw new BusinessRuleException("Fecha de gasto fuera del rango del viático");
            }
            return GastoMapper.toEntity(viaticoId, i, new GastoEntity());
        }).toList();

        var saved = repo.saveAll(entities);
        return saved.stream().map(GastoMapper::toDto).toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<GastoResponseDTO> listByViatico(Long viaticoId) {
        return repo.findByViaticoId(viaticoId).stream().map(GastoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GastoResponseDTO get(Long id) {
        return repo.findById(id).map(GastoMapper::toDto)
                .orElseThrow(() -> new GastoNotFoundException(id));
    }

    @Override
    @Transactional
    public GastoResponseDTO update(Long id, GastoUpdateDTO dto) {
        var entity = repo.findById(id).orElseThrow(() -> new GastoNotFoundException(id));
        var viatico = viaticoClient.get(entity.getViaticoId());
        if ("RECHAZADO".equalsIgnoreCase(viatico.estado()))
            throw new BusinessRuleException("No se pueden modificar gastos porque el viático está RECHAZADO");
        if (dto.fecha().isBefore(viatico.fechaInicio()) || dto.fecha().isAfter(viatico.fechaFin())) {
            throw new BusinessRuleException("La fecha del gasto está fuera del rango del viático");
        }
        GastoMapper.toEntity(dto, entity);
        return GastoMapper.toDto(repo.save(entity));
    }

    @Override
    @Transactional
    public GastoResponseDTO delete(Long id) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new GastoNotFoundException(id));
        var viatico = viaticoClient.get(entity.getViaticoId());
        if ("RECHAZADO".equalsIgnoreCase(viatico.estado())) {
            throw new BusinessRuleException("No se pueden eliminar gastos porque el viático está RECHAZADO");
        }
        var dto = GastoMapper.toDto(entity);
        repo.delete(entity);
        return dto;
    }

}
