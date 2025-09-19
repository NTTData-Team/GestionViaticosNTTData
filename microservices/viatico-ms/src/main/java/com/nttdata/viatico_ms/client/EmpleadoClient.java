package com.nttdata.viatico_ms.client;

import com.nttdata.viatico_ms.client.dto.ApiResponse;
import com.nttdata.viatico_ms.client.dto.EmployeeResponseDTO;
import com.nttdata.viatico_ms.client.dto.ProjectResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "empleado-ms")
public interface EmpleadoClient {

    @GetMapping("/api/employees/{id}")
    ApiResponse<EmployeeResponseDTO> getEmployee(@PathVariable("id") Long id);

    @GetMapping("/api/projects/{id}")
    ApiResponse<ProjectResponseDTO> getProject(@PathVariable("id") Long id);
}
