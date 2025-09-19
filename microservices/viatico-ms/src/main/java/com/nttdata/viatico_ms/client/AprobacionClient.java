package com.nttdata.viatico_ms.client;

import com.nttdata.viatico_ms.client.dto.AprobacionCreateDTO;
import com.nttdata.viatico_ms.client.dto.AprobacionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aprobacion-ms")
public interface AprobacionClient {

    @PostMapping("/api/aprobaciones")
    AprobacionResponseDTO create(@RequestBody AprobacionCreateDTO dto);
}
