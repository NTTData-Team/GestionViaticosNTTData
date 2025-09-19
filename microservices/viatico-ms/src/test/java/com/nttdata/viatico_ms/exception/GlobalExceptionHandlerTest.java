package com.nttdata.viatico_ms.exception;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mvc;

    @RestController
    static class DummyController {
        @GetMapping("/ref-not-found")
        public void refNF() { throw new ReferenceNotFoundException("Empleado", 999L); }

        @GetMapping("/feign-404")
        public void feign404() {
            Request req = Request.create(Request.HttpMethod.GET, "/remote", Map.of(), null, new RequestTemplate());
            throw new FeignException.NotFound("NF", req, "{\"title\":\"Empleado no existe\"}".getBytes(StandardCharsets.UTF_8), Map.of());
        }

        @GetMapping("/unexpected")
        public void unexpected(){ throw new RuntimeException("boom"); }
    }

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void referenceNotFound_404_problemDetail() throws Exception {
        mvc.perform(get("/ref-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("urn:problem:reference-not-found")))
                .andExpect(jsonPath("$.title", is("Referencia no encontrada")))
                .andExpect(jsonPath("$.detail", containsString("Empleado")))
                .andExpect(jsonPath("$.resource", is("Empleado")))
                .andExpect(jsonPath("$.id", is(999)));
    }

    @Test
    void feign_downstream_passthrough_status_y_cuerpo() throws Exception {
        mvc.perform(get("/feign-404"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Error al invocar servicio remoto")))
                .andExpect(jsonPath("$.downstreamStatus", is(404)))
                .andExpect(jsonPath("$.detail", containsString("Empleado no existe")));
    }

    @Test
    void unexpected_500_problemDetail() throws Exception {
        mvc.perform(get("/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title", is("Internal Server Error")))
                .andExpect(jsonPath("$.detail", is("Ocurrió un error inesperado")));
    }
}
