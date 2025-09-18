package com.nttdata.empleado_ms.mapper;

import com.nttdata.empleado_ms.model.dto.ProjectRequestDTO;
import com.nttdata.empleado_ms.model.dto.ProjectResponseDTO;
import com.nttdata.empleado_ms.model.entity.ProjectEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectEntity toEntity(ProjectRequestDTO dto);

    ProjectResponseDTO toResponse(ProjectEntity entity);

    List<ProjectResponseDTO> toResponseList(List<ProjectEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectRequestDTO dto, @MappingTarget ProjectEntity entity);
}
