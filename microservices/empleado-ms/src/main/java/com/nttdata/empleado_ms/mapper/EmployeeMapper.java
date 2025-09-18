package com.nttdata.empleado_ms.mapper;

import com.nttdata.empleado_ms.model.dto.EmployeeRequestDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeResponseDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeUpdateDTO;
import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeEntity toEntity(EmployeeRequestDTO dto);
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.name", target = "areaName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    EmployeeResponseDTO toResponse(EmployeeEntity entity);
    List<EmployeeResponseDTO> toResponseList(List<EmployeeEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EmployeeUpdateDTO dto, @MappingTarget EmployeeEntity entity);
}
