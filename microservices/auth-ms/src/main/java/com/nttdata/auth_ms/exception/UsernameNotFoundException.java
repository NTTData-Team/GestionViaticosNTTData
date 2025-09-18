package com.nttdata.auth_ms.exception;

import lombok.Getter;

@Getter
public class UsernameNotFoundException extends RuntimeException {

    private final String username;

    public UsernameNotFoundException(String username) {
        super("El usuario "+username+" no fue encontrado");
        this.username = username;
    }
}
