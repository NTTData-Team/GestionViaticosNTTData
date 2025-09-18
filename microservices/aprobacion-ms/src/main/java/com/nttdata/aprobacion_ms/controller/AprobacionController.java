package com.nttdata.aprobacion_ms.controller;

import com.nttdata.aprobacion_ms.model.dto.*;
import com.nttdata.aprobacion_ms.service.AprobacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/aprobaciones")
@RequiredArgsConstructor
public class AprobacionController {

    private final AprobacionService service;
    @PostMapping
    public ResponseEntity<AprobacionResponseDTO> create(@Valid @RequestBody AprobacionCreateDTO dto) {
        var created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();

        return ResponseEntity.created(location)
                .header("X-Message", "Aprobación creada con éxito")
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AprobacionResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/viatico/{viaticoId}")
    public ResponseEntity<List<AprobacionResponseDTO>> listByViatico(@PathVariable Long viaticoId) {
        var list = service.listByViatico(viaticoId);
        return ResponseEntity.ok()
                .header("X-Message", "Historial de aprobaciones por viático")
                .header("X-Count", String.valueOf(list.size()))
                .body(list);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AprobacionResponseDTO>> pendientes(@RequestParam Long aprobadorId) {
        var list = service.listPendientesByAprobador(aprobadorId);
        return ResponseEntity.ok()
                .header("X-Message", "Aprobaciones pendientes para el aprobador")
                .header("X-Count", String.valueOf(list.size()))
                .body(list);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<AprobacionResponseDTO> approve(@PathVariable Long id,
                                                         @Valid @RequestBody AprobacionDecisionDTO dto) {
        var updated = service.approve(id, dto);
        return ResponseEntity.ok()
                .header("X-Message", "Aprobación registrada con éxito")
                .body(updated);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<AprobacionResponseDTO> reject(@PathVariable Long id,
                                                        @Valid @RequestBody AprobacionDecisionDTO dto) {
        var updated = service.reject(id, dto);
        return ResponseEntity.ok()
                .header("X-Message", "Rechazo registrado con éxito")
                .body(updated);
    }
}