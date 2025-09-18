package com.nttdata.gasto_ms.repository;

import com.nttdata.gasto_ms.model.entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoRepository extends JpaRepository<GastoEntity, Long> {
    List<GastoEntity> findByViaticoId(Long viaticoId);
}