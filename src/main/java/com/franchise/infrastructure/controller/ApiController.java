package com.franchise.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Franchise API - REST API para Gestión de Franquicias");
        response.put("version", "1.0.0");
        response.put("endpoints", new HashMap<String, String>() {{
            put("GET /api/franchises", "Obtener todas las franquicias");
            put("POST /api/franchises", "Crear nueva franquicia");
            put("GET /api/franchises/{id}", "Obtener franquicia por ID");
            put("POST /api/franchises/{id}/branches", "Agregar sucursal");
            put("POST /api/franchises/{id}/branches/{branchId}/products", "Agregar producto");
            put("PUT /api/franchises/{id}/branches/{branchId}/products/{productId}/stock", "Actualizar stock");
            put("GET /api/franchises/{id}/top-stock", "Top stock por sucursal");
            put("DELETE /api/franchises/{id}/branches/{branchId}/products/{productId}", "Eliminar producto");
            put("PUT /api/franchises/{id}/rename", "Renombrar franquicia");
            put("PUT /api/franchises/{id}/branches/{branchId}/rename", "Renombrar sucursal");
            put("PUT /api/franchises/{id}/branches/{branchId}/products/{productId}/rename", "Renombrar producto");
        }});
        return Mono.just(ResponseEntity.ok(response));
    }
}
