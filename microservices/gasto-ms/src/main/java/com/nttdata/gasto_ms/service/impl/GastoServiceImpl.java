package com.nttdata.gasto_ms.service.impl;

import com.nttdata.gasto_ms.exception.GastoNotFoundException;
import com.nttdata.gasto_ms.model.dto.GastoCreateDTO;
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

    @Override
    @Transactional
    public GastoResponseDTO create(GastoCreateDTO dto) {
        log.debug("Creando gasto viaticoId={}", dto.viaticoId());
        GastoEntity entity = GastoMapper.toEntity(dto, new GastoEntity());
        return GastoMapper.toDto(repo.save(entity));
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
        GastoEntity entity = repo.findById(id).orElseThrow(() -> new GastoNotFoundException(id));
        GastoMapper.toEntity(dto, entity);
        return GastoMapper.toDto(repo.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new GastoNotFoundException(id);
        repo.deleteById(id);
    }
}
