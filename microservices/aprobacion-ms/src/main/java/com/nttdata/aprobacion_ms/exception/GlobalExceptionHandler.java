package com.nttdata.aprobacion_ms.exception;

import com.nttdata.aprobacion_ms.error.ProblemDetailsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static com.nttdata.aprobacion_ms.error.ProblemDetailsUtils.of;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AprobacionNotFoundException.class)
    public ProblemDetail notFound(AprobacionNotFoundException ex, HttpServletRequest req) {
        return of(HttpStatus.NOT_FOUND,
                "Aprobación no encontrada",
                ex.getMessage(),
                req,
                "APROBACION_NOT_FOUND",
                "urn:problem:aprobacion-not-found");
    }
    @ExceptionHandler(DuplicateApprovalException.class)
    public ResponseEntity<ProblemDetail> handleDup(DuplicateApprovalException ex, HttpServletRequest req) {
        var pd = ProblemDetailsUtils.of(
                HttpStatus.CONFLICT,
                "Aprobación duplicada",
                ex.getMessage(),
                req,
                "DUPLICATE_APPROVAL",
                "urn:problem:duplicate-approval"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }
    @ExceptionHandler(InvalidDecisionException.class)
    public ResponseEntity<ProblemDetail> invalidDecision(InvalidDecisionException ex, HttpServletRequest req) {
        var pd = of(HttpStatus.CONFLICT,
                "Transición de estado inválida",
                ex.getMessage(),
                req,
                "INVALID_DECISION",
                "urn:problem:invalid-decision");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var pd = of(HttpStatus.BAD_REQUEST,
                "Error de validación",
                "Revise los campos enviados",
                req,
                "VALIDATION_ERROR",
                "urn:problem:validation");
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleJsonMalformed(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var pd = of(HttpStatus.BAD_REQUEST,
                "JSON mal formado",
                ProblemDetailsUtils.conciseJacksonMessage(ex),
                req,
                "MALFORMED_JSON",
                "urn:problem:malformed-json");
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor válido";
        var pd = of(HttpStatus.BAD_REQUEST,
                "Parámetro inválido",
                "El parámetro '" + ex.getName() + "' tiene un formato inválido. Se esperaba " + expected + ".",
                req,
                "PARAM_TYPE_MISMATCH",
                "urn:problem:param-type-mismatch");
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var pd = of(HttpStatus.BAD_REQUEST,
                "Parámetro inválido",
                "Uno o más parámetros son inválidos",
                req,
                "CONSTRAINT_VIOLATION",
                "urn:problem:constraint-violation");
        var errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of("param", v.getPropertyPath().toString(), "message", v.getMessage()))
                .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        String allowed = ex.getSupportedMethods() != null ? String.join(", ", ex.getSupportedMethods()) : "método permitido";
        var pd = of(HttpStatus.METHOD_NOT_ALLOWED,
                "Método no permitido",
                "Use uno de: " + allowed,
                req,
                "METHOD_NOT_ALLOWED",
                "urn:problem:method-not-allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(pd);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) {
        var pd = of(HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                "No existe el endpoint solicitado.",
                req,
                "NO_RESOURCE_FOUND",
                "urn:problem:not-found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex, HttpServletRequest req) {
        var pd = of(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Ocurrió un error inesperado",
                req,
                "UNEXPECTED_ERROR",
                "urn:problem:unexpected");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}
