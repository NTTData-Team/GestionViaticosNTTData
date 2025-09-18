package com.nttdata.aprobacion_ms.exception;

public class DuplicateApprovalException extends RuntimeException {
    public DuplicateApprovalException(Long viaticoId, Integer nivel) {
        super("Ya existe una aprobación para viático=" + viaticoId + " y nivel=" + nivel);
    }
}

