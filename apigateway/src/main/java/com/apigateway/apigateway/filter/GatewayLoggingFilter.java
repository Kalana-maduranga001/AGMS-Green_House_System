package com.apigateway.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

/**
 * Simple request/response logger that helps debug missing routes and error
 * codes.
 */
@Component
public class GatewayLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GatewayLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();

        log.info("Gateway request: {} {}", request.getMethod(), requestUri.getPath());

        return chain.filter(exchange)
                .doOnSuccess(unused -> logExchange(exchange, request, requestUri, null))
                .doOnError(throwable -> logExchange(exchange, request, requestUri, throwable));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private void logExchange(ServerWebExchange exchange,
            ServerHttpRequest request,
            URI requestUri,
            Throwable throwable) {
        HttpStatusCode status = Optional.ofNullable(exchange.getResponse().getStatusCode())
                .orElse(HttpStatus.OK);

        if (throwable != null) {
            log.error("Gateway error: {} {} -> {}", request.getMethod(), requestUri.getPath(), status, throwable);
            return;
        }

        log.info("Gateway response: {} {} -> {}", request.getMethod(), requestUri.getPath(), status);

        if (status.value() == HttpStatus.NOT_FOUND.value()) {
            log.warn("Route not found for {} {}. Verify route predicates and Eureka registrations.",
                    request.getMethod(), requestUri.getPath());
        }
    }
}
