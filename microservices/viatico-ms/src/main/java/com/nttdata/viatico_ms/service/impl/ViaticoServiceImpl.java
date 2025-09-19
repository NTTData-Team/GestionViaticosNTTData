package com.nttdata.viatico_ms.service.impl;

import com.nttdata.viatico_ms.client.AprobacionClient;
import com.nttdata.viatico_ms.client.EmpleadoClient;
import com.nttdata.viatico_ms.client.GastoClient;
import com.nttdata.viatico_ms.client.dto.*;
import com.nttdata.viatico_ms.exception.ReferenceNotFoundException;
import com.nttdata.viatico_ms.exception.ViaticoNotFoundException;
import com.nttdata.viatico_ms.model.dto.ViaticoCreateDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoCreateWithGastosDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoResponseDTO;
import com.nttdata.viatico_ms.model.entity.ViaticoEntity;
import com.nttdata.viatico_ms.model.mapper.ViaticoMapper;
import com.nttdata.viatico_ms.repository.ViaticoRepository;
import com.nttdata.viatico_ms.service.ViaticoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViaticoServiceImpl implements ViaticoService {

    private final ViaticoRepository repo;
    private final EmpleadoClient empleadoClient;
    private final AprobacionClient aprobacionClient;
    private final GastoClient gastoClient;

    @Override
    @Transactional
    public ViaticoResponseDTO create(ViaticoCreateWithGastosDTO dto) {
        var empleado = empleadoClient.getEmployee(dto.empleadoId());
        if (empleado == null || empleado.getData() == null) {
            throw new ReferenceNotFoundException("Empleado", dto.empleadoId());
        }
        var proyecto = empleadoClient.getProject(dto.proyectoId());
        if (proyecto == null || proyecto.getData() == null) {
            throw new ReferenceNotFoundException("Proyecto", dto.proyectoId());
        }
        var entity = ViaticoMapper.toEntity(
                new ViaticoCreateDTO(
                        empleado.getData().id(), proyecto.getData().id(), dto.fechaInicio(), dto.fechaFin(),
                        dto.destino(), dto.montoEstimado(), dto.moneda(), dto.motivo()
                ),
                new ViaticoEntity()
        );
        entity = repo.save(entity);

        aprobacionClient.create(new AprobacionCreateDTO(entity.getId(), 1));
        entity.setEstado(ViaticoEntity.Estado.EN_APROBACION);
        entity = repo.save(entity);

        final Long viaticoId = entity.getId();
        var gastos = dto.gastosIniciales();
        if (gastos != null && !gastos.isEmpty()) {
            try {
                var mapped = gastos.stream()
                        .map(i -> new GastoItemCreateDTO(
                                i.categoria(), i.fecha(), i.monto(), i.moneda(),
                                i.descripcion(), i.comprobanteUrl()))
                        .toList();
                gastoClient.createBatch(viaticoId, new GastoBatchCreateDTO(mapped));
            } catch (Exception ex) {
                log.warn("Gastos iniciales no pudieron registrarse para viaticoId={}: {}", viaticoId, ex.getMessage());
            }
        }

        return ViaticoMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ViaticoResponseDTO get(Long id) {
        return repo.findById(id).map(ViaticoMapper::toDto)
                .orElseThrow(() -> new ViaticoNotFoundException(id));
    }
    @Override
    @Transactional
    public ViaticoResponseDTO updateEstado(Long id, String nuevoEstado) {
        var entity = repo.findById(id).orElseThrow(() -> new ViaticoNotFoundException(id));
        var estado = ViaticoEntity.Estado.valueOf(nuevoEstado); // lanza IAE si no coincide
        entity.setEstado(estado);
        entity = repo.save(entity);
        return ViaticoMapper.toDto(entity);
    }

}
