package com.nttdata.aprobacion_ms.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ProblemDetailsUtils {
    private ProblemDetailsUtils() {}

    public static ProblemDetail of(HttpStatus status,
                                   String title,
                                   String detail,
                                   HttpServletRequest req,
                                   String code,
                                   String typeUrn) {
        var pd = ProblemDetail.forStatus(status);
        if (title != null) pd.setTitle(title);
        if (detail != null) pd.setDetail(detail);
        if (req != null) pd.setInstance(URI.create(req.getRequestURI()));
        if (typeUrn != null) pd.setType(URI.create(typeUrn));
        if (code != null) pd.setProperty("code", code);
        return pd;
    }

    public static String conciseJacksonMessage(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException ife) {
            String field = ife.getPath() != null && !ife.getPath().isEmpty()
                    ? ife.getPath().stream()
                    .map(ref -> Objects.toString(ref.getFieldName(), "?"))
                    .collect(Collectors.joining("."))
                    : "?";
            String expected = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "tipo esperado";
            return "El campo '" + field + "' tiene un valor con formato inválido. Se esperaba " + expected + ".";
        }
        return (cause != null && cause.getMessage() != null)
                ? cause.getMessage()
                : "El cuerpo de la solicitud no es un JSON válido o no coincide con el esquema esperado.";
    }
}
