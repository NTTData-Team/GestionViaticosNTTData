package com.nttdata.empleado_ms.repository;

import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
    Optional<EmployeeEntity> findByUserId(Long userId);
    Optional<EmployeeEntity> findByCodeEmployee(String codeEmployee);
    Optional<EmployeeEntity> findByDocumentIdentity(String documentIdentity);

    boolean existsByCodeEmployee(String codeEmployee);
    boolean existsByDocumentIdentity(String documentIdentity);

    List<EmployeeEntity> findAllByActiveTrue();
    Optional<EmployeeEntity> findByUserIdAndActiveTrue(Long userId);
    boolean existsByCodeEmployeeAndActiveTrue(String codeEmployee);

    List<EmployeeEntity> findAllByAreaIdAndActiveTrue(Long areaId);
    List<EmployeeEntity> findAllByProjectIdAndActiveTrue(Long projectId);

    Optional<EmployeeEntity> findByDocumentIdentityAndActiveTrue(String documentIdentity);
    boolean existsByUserIdAndActiveTrue(Long userId);
}
