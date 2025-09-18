package com.nttdata.gasto_ms.controller;

import com.nttdata.gasto_ms.model.dto.GastoBatchCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoCreateDTO;
import com.nttdata.gasto_ms.model.dto.GastoResponseDTO;
import com.nttdata.gasto_ms.model.dto.GastoUpdateDTO;
import com.nttdata.gasto_ms.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService service;

    @PostMapping
    public ResponseEntity<GastoResponseDTO> create(@Valid @RequestBody GastoCreateDTO dto) {
        GastoResponseDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
    @PostMapping("/viatico/{viaticoId}/batch")
    public ResponseEntity<List<GastoResponseDTO>> createBatch(@PathVariable Long viaticoId, @Valid @RequestBody GastoBatchCreateDTO dto) {
        var list = service.createBatch(viaticoId, dto.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }
    @GetMapping("/viatico/{viaticoId}")
    public ResponseEntity<List<GastoResponseDTO>> listByViatico(@PathVariable Long viaticoId) {
        List<GastoResponseDTO> gastos = service.listByViatico(viaticoId);
        return ResponseEntity.ok(gastos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> get(@PathVariable Long id) {
        GastoResponseDTO gasto = service.get(id);
        return ResponseEntity.ok(gasto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody GastoUpdateDTO dto) {
        GastoResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> delete(@PathVariable Long id) {
        GastoResponseDTO deleted = service.delete(id); // cambia el service a devolver DTO
        return ResponseEntity.ok(deleted);
    }
}
