package com.nttdata.gasto_ms.exception;

public class GastoNotFoundException extends RuntimeException {
    public GastoNotFoundException(Long id){
        super("Gasto no encontrado: " + id);
    }
}
