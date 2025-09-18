package com.nttdata.viatico_ms.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ViaticoNotFoundException.class)
    public ProblemDetail notFound(ViaticoNotFoundException ex){
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Viático no encontrado");
        pd.setDetail(ex.getMessage());
        return pd;
    }
    @ExceptionHandler(ReferenceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleRefNotFound(ReferenceNotFoundException ex, HttpServletRequest req) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Referencia no encontrada");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setType(URI.create("urn:problem:reference-not-found"));
        pd.setProperty("resource", ex.getResource());
        pd.setProperty("id", ex.getId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handleFeign(FeignException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.BAD_GATEWAY;
        var pd = ProblemDetail.forStatus(status);
        pd.setTitle("Error al invocar servicio remoto");
        String body = null;
        try { body = ex.contentUTF8(); } catch (Exception ignore) {}
        pd.setDetail(body != null && !body.isBlank() ? body : ex.getMessage());
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setType(URI.create("urn:problem:downstream-error"));
        pd.setProperty("downstreamStatus", ex.status());
        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail unexpected(Exception ex){
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setDetail("Ocurrió un error inesperado");
        return pd;
    }
}
