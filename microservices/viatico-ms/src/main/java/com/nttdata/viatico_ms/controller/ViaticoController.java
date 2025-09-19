package com.nttdata.viatico_ms.controller;

import com.nttdata.viatico_ms.model.dto.ViaticoCreateWithGastosDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoEstadoUpdateDTO;
import com.nttdata.viatico_ms.model.dto.ViaticoResponseDTO;
import com.nttdata.viatico_ms.service.ViaticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/viaticos")
@RequiredArgsConstructor
public class ViaticoController {

    private final ViaticoService service;

    @PostMapping
    public ResponseEntity<ViaticoResponseDTO> create(@Valid @RequestBody ViaticoCreateWithGastosDTO dto) {
        var created = service.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }
    @GetMapping("/{id}")
    public ViaticoResponseDTO get(@PathVariable Long id){
        return service.get(id);
    }
    @PatchMapping("/{id}/estado")
    public ViaticoResponseDTO updateEstado(@PathVariable Long id, @Valid @RequestBody ViaticoEstadoUpdateDTO dto) {
        return service.updateEstado(id, dto.estado());
    }
}
