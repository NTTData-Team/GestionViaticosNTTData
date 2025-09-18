package com.nttdata.auth_ms.mapper;

import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;
import com.nttdata.auth_ms.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(UserRequestDTO dto);
    UserResponseDTO toResponse(UserEntity entity);
    List<UserResponseDTO> toResponseList(List<UserEntity> entities);
    void updateEntityFromDto(UserRequestDTO dto, @MappingTarget UserEntity entity);
}
