package com.nttdata.empleado_ms.controller;

import com.nttdata.empleado_ms.model.dto.AreaRequestDTO;
import com.nttdata.empleado_ms.model.dto.AreaResponseDTO;
import com.nttdata.empleado_ms.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @PostMapping
    public AreaResponseDTO create(@Valid @RequestBody AreaRequestDTO dto) {
        System.out.println(dto);
        return areaService.create(dto);
    }

    @GetMapping
    public List<AreaResponseDTO> findAll() {
        return areaService.findAll();
    }

    @GetMapping("/{id}")
    public AreaResponseDTO findById(@PathVariable Long id) {
        return areaService.findById(id);
    }
}
