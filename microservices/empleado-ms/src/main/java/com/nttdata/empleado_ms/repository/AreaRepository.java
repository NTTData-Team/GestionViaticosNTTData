package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity,Long> {
    boolean existsByName(String name);
}
