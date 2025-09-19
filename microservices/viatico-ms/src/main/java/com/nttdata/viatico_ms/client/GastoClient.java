package com.nttdata.viatico_ms.client;

import com.nttdata.viatico_ms.client.dto.GastoBatchCreateDTO;
import com.nttdata.viatico_ms.client.dto.GastoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "gasto-ms")
public interface GastoClient {
    @GetMapping("/api/gastos/viatico/{viaticoId}")
    List<GastoResponseDTO> listByViatico(@PathVariable("viaticoId") Long viaticoId);
    @PostMapping("/api/gastos/viatico/{viaticoId}/batch")
    void createBatch(@PathVariable("viaticoId") Long viaticoId,
                     @RequestBody GastoBatchCreateDTO dto);
}
