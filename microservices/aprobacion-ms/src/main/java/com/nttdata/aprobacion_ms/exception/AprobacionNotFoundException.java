package com.nttdata.aprobacion_ms.exception;

public class AprobacionNotFoundException extends RuntimeException {
    public AprobacionNotFoundException(Long id) {
        super("Aprobación no encontrada: " + id);
    }
}
