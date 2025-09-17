package com.nttdata.empleado_ms.mapper;

import com.nttdata.empleado_ms.model.dto.EmployeeRequestDTO;
import com.nttdata.empleado_ms.model.dto.EmployeeResponseDTO;
import com.nttdata.empleado_ms.model.entity.EmployeeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeEntity toEntity(EmployeeRequestDTO dto);
    EmployeeResponseDTO toResponse(EmployeeEntity entity);
    List<EmployeeResponseDTO> toResponseList(List<EmployeeEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EmployeeRequestDTO dto, @MappingTarget EmployeeEntity entity);
}
