package com.nttdata.empleado_ms.mapper;

import com.nttdata.empleado_ms.model.dto.AreaRequestDTO;
import com.nttdata.empleado_ms.model.dto.AreaResponseDTO;
import com.nttdata.empleado_ms.model.entity.AreaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AreaMapper {
    AreaEntity toEntity(AreaRequestDTO dto);
    AreaResponseDTO toResponse(AreaEntity entity);
    List<AreaResponseDTO> toResponseList(List<AreaEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AreaRequestDTO dto, @MappingTarget AreaEntity entity);
}
