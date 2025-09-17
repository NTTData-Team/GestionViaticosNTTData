package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository  extends JpaRepository<ProjectEntity,Long> {
    boolean existsByName(String name);
    boolean existsByNameAndActiveTrue(String name);
    List<ProjectEntity> findAllByActiveTrue();
}
