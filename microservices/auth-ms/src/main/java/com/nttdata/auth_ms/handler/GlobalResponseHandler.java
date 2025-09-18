package com.nttdata.auth_ms.handler;

import com.nttdata.auth_ms.model.dto.ApiResponse;
import com.nttdata.auth_ms.model.dto.CustomErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body instanceof CustomErrorResponse) {
            return body;
        }

        if (body instanceof org.springframework.http.ResponseEntity<?>) {
            return body;
        }

        String method = request.getMethod().toString();

        if ("DELETE".equalsIgnoreCase(method)) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
            return null;
        }

        int status = HttpStatus.OK.value();
        String message = "Operación exitosa";

        if ("POST".equalsIgnoreCase(method)) {
            status = HttpStatus.CREATED.value();
            message = "Recurso creado exitosamente";
        } else if ("PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            message = "Recurso actualizado exitosamente";
        }

        return new ApiResponse<>(status, message, body);
    }
}