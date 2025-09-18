package com.nttdata.auth_ms.controller;

import com.nttdata.auth_ms.model.dto.UserRequestDTO;
import com.nttdata.auth_ms.model.dto.UserResponseDTO;
import com.nttdata.auth_ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalAuthController {

    private final UserService userService;

    @PostMapping("/users")
    public UserResponseDTO createUserInternal(@RequestBody @Valid UserRequestDTO dto) {
        return userService.createUser(dto);
    }
}
