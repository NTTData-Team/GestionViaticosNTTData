package com.nttdata.gasto_ms.client;

import com.nttdata.gasto_ms.client.dto.ViaticoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viatico-ms", contextId = "ViaticoClientGasto")
public interface ViaticoClient {
    @GetMapping("/api/viaticos/{id}")
    ViaticoResponseDTO get(@PathVariable("id") Long id);
}

