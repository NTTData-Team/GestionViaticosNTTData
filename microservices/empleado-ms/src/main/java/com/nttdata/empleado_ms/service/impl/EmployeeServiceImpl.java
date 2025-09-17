package com.nttdata.empleado_ms.service.impl;

import com.nttdata.empleado_ms.exception.ResourceNotFoundException;
import com.nttdata.empleado_ms.model.dto.EmployeeRequestDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeResponseDTO;
import com.nttdata.empleado_ms.model.entity.AreaEntity;
import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import com.nttdata.empleado_ms.repository.AreaRepository;
import com.nttdata.empleado_ms.mapper.EmployeeMapper;
import com.nttdata.empleado_ms.repository.EmployeeRepository;
import com.nttdata.empleado_ms.repository.ProjectRepository;
import com.nttdata.empleado_ms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final AreaRepository areaRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {

        if(employeeRepository.existsByCodeEmployee(dto.getCodeEmployee()))
            throw new IllegalArgumentException("Ya existe un empleado con ese código");
        if(employeeRepository.existsByDocumentIdentity(dto.getDocumentIdentity()))
            throw new IllegalArgumentException("Ya existe un empleado con ese documento");

        ProjectEntity project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + dto.getProjectId()));

        AreaEntity area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con id: " + dto.getAreaId()));

        EmployeeEntity employee = employeeMapper.toEntity(dto);
        employee.setProject(project);
        employee.setArea(area);
        employee.setActive(true);

        EmployeeEntity saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    @Override
    public EmployeeResponseDTO findById(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return employeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        if(!employee.getCodeEmployee().equals(dto.getCodeEmployee()) &&
                employeeRepository.existsByCodeEmployee(dto.getCodeEmployee()))
            throw new IllegalArgumentException("Ya existe un empleado con ese código");
        if(!employee.getDocumentIdentity().equals(dto.getDocumentIdentity()) &&
                employeeRepository.existsByDocumentIdentity(dto.getDocumentIdentity()))
            throw new IllegalArgumentException("Ya existe un empleado con ese documento");

        ProjectEntity project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + dto.getProjectId()));

        AreaEntity area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con id: " + dto.getAreaId()));

        employeeMapper.updateEntityFromDto(dto, employee);
        employee.setProject(project);
        employee.setArea(area);

        EmployeeEntity updated = employeeRepository.save(employee);
        return employeeMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public boolean existsByDocumentIdentity(String documentIdentity) {
        return employeeRepository.existsByDocumentIdentity(documentIdentity);
    }

    @Override
    public boolean existsByCodeEmployee(String codeEmployee) {
        return employeeRepository.existsByCodeEmployee(codeEmployee);
    }
}
