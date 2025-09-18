package com.nttdata.auth_ms.controller;


import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;
import com.nttdata.auth_ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/email")
    public UserResponseDTO getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/exists")
    public boolean existsByEmail(@RequestParam String email) {
        return userService.existsByEmail(email);
    }
}
