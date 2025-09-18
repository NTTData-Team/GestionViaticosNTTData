package com.nttdata.viatico_ms.exception;

public class ViaticoNotFoundException extends RuntimeException {
    public ViaticoNotFoundException(Long id) {
        super("Viático no encontrado: " + id);
    }
}
