package com.nttdata.gasto_ms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GastoNotFoundException.class)
    public ProblemDetail notFound(GastoNotFoundException ex, HttpServletRequest req) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Gasto no encontrado");
        pd.setType(URI.create("urn:problem:gasto-not-found")); // o https://tu.api/errors/gasto-not-found
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("code", "GASTO_NOT_FOUND");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Error de validación");
        pd.setDetail("Revise los campos enviados");
        pd.setInstance(URI.create(req.getRequestURI()));
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();
        pd.setProperty("errors", errors);
        pd.setProperty("code", "VALIDATION_ERROR");
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Solicitud inválida");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("code", "BAD_REQUEST");
        return ResponseEntity.badRequest().body(pd);
    }
}

