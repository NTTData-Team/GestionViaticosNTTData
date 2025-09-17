package com.nttdata.empleado_ms.service;

import com.nttdata.empleado_ms.model.dto.EmployeeRequestDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeResponseDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeUpdateDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);

    List<EmployeeResponseDTO> findAll();

    EmployeeResponseDTO findById(Long id);

    EmployeeResponseDTO update(Long id, EmployeeUpdateDTO dto);

    void delete(Long id);

    boolean existsByDocumentIdentity(String documentIdentity);

    boolean existsByCodeEmployee(String codeEmployee);
}
