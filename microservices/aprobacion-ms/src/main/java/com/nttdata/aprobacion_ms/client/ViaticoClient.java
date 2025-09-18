package com.nttdata.aprobacion_ms.client;

import com.nttdata.aprobacion_ms.client.dto.ViaticoEstadoUpdateDTO;
import com.nttdata.aprobacion_ms.client.dto.ViaticoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "viatico-ms", contextId = "ViaticoClient")
public interface ViaticoClient {

    @PatchMapping("/internal/viaticos/{id}/estado")
    ViaticoResponseDTO updateEstado(@PathVariable("id") Long id,
                                    @RequestBody ViaticoEstadoUpdateDTO dto);
}
