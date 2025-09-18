package com.nttdata.empleado_ms.client;

import com.nttdata.empleado_ms.model.dto.ApiResponse;
import com.nttdata.empleado_ms.model.dto.UserRequestDTO;
import com.nttdata.empleado_ms.model.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-ms")
public interface AuthClient {
    @PostMapping("/internal/users")
    ApiResponse<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto);
}
