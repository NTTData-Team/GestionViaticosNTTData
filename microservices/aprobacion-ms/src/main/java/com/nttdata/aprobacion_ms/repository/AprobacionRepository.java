package com.nttdata.aprobacion_ms.repository;

import com.nttdata.aprobacion_ms.model.entity.AprobacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AprobacionRepository extends JpaRepository<AprobacionEntity, Long> {
    List<AprobacionEntity> findByViaticoId(Long viaticoId);
    List<AprobacionEntity> findByEstadoAndAprobadorId(AprobacionEntity.Estado estado, Long aprobadorId);
}
