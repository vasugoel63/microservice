package com.example.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    // Public APIs (NO auth required)
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/create");

    // Predicate → returns true if request needs security
    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
            .stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}