package com.nttdata.api_gateway.jwt;

import com.nttdata.api_gateway.config.SecurityProperties;
import com.nttdata.api_gateway.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityProperties.Rule publicRule = new SecurityProperties.Rule();
        publicRule.setPath("/public/**");
        publicRule.setRoles(Collections.emptyList());

        when(securityProperties.getRules()).thenReturn(Collections.singletonList(publicRule));

        filter = new JwtAuthenticationFilter(securityProperties);
    }

    @Test
    void testPublicPathPassesThrough() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getURI()).thenReturn(URI.create("/public/test"));
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();
        verify(chain).filter(exchange);
    }

    @Test
    void testUnauthorizedWithoutToken() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getURI()).thenReturn(URI.create("/private/test"));
        when(request.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(exchange.getResponse()).thenReturn(response);
        when(response.setStatusCode(any())).thenReturn(true);
        when(response.setComplete()).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();
        verify(response).setStatusCode(any());
        verify(response).setComplete();
    }
}