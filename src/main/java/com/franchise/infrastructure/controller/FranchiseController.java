package com.franchise.infrastructure.controller;

import com.franchise.application.service.FranchiseService;
import com.franchise.domain.entity.Franchise;
import com.franchise.infrastructure.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@Slf4j
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    // RF-01: Agregar Franquicia
    @PostMapping
    public Mono<ResponseEntity<FranchiseResponse>> addFranchise(@RequestBody FranchiseRequest request) {
        return franchiseService.addFranchise(request.getName())
                .map(this::toFranchiseResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(e -> {
                    log.error("Error creating franchise: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    // RF-02: Agregar Sucursal
    @PostMapping("/{franchiseId}/branches")
    public Mono<ResponseEntity<FranchiseResponse>> addBranch(
            @PathVariable String franchiseId,
            @RequestBody BranchRequest request) {
        return franchiseService.addBranch(franchiseId, request.getName())
                .map(this::toFranchiseResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(e -> {
                    log.error("Error adding branch to franchise {}: {}", franchiseId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    // RF-03: Agregar Producto
    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<ResponseEntity<FranchiseResponse>> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestBody ProductRequest request) {
        return franchiseService.addProduct(franchiseId, branchId, request.getName(), request.getStock())
                .map(this::toFranchiseResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(e -> {
                    log.error("Error adding product to branch {}: {}", branchId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    // RF-04: Eliminar Producto
    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<Void>> deleteProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId) {
        return franchiseService.deleteProduct(franchiseId, branchId, productId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error deleting product {}: {}", productId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    // RF-05: Modificar Stock
    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<ProductResponse>> updateStock(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody UpdateStockRequest request) {
        return franchiseService.updateStock(franchiseId, branchId, productId, request.getStock())
                .flatMap(franchise -> {
                    var branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));
                    var product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return Mono.just(ResponseEntity.ok(toProductResponse(product)));
                })
                .onErrorResume(e -> {
                    log.error("Error updating stock for product {}: {}", productId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    // RF-06: Top Stock por Sucursal
    @GetMapping("/{franchiseId}/top-stock")
    public Flux<TopStockResponse> getTopStockByBranch(@PathVariable String franchiseId) {
        return franchiseService.getTopStockByBranch(franchiseId);
    }

    // RF-07: Renombrar Franquicia
    @PutMapping("/{franchiseId}/rename")
    public Mono<ResponseEntity<FranchiseResponse>> renameFranchise(
            @PathVariable String franchiseId,
            @RequestBody RenameRequest request) {
        return franchiseService.renameFranchise(franchiseId, request.getNewName())
                .map(this::toFranchiseResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // RF-07: Renombrar Sucursal
    @PutMapping("/{franchiseId}/branches/{branchId}/rename")
    public Mono<ResponseEntity<FranchiseResponse>> renameBranch(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestBody RenameRequest request) {
        return franchiseService.renameBranch(franchiseId, branchId, request.getNewName())
                .map(this::toFranchiseResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // RF-07: Renombrar Producto
    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/rename")
    public Mono<ResponseEntity<FranchiseResponse>> renameProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody RenameRequest request) {
        return franchiseService.renameProduct(franchiseId, branchId, productId, request.getNewName())
                .map(this::toFranchiseResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // Utility: Get all franchises
    @GetMapping
    public Flux<FranchiseResponse> getAllFranchises() {
        return franchiseService.getAllFranchises()
                .map(this::toFranchiseResponse);
    }

    // Utility: Get franchise by ID
    @GetMapping("/{franchiseId}")
    public Mono<ResponseEntity<FranchiseResponse>> getFranchiseById(@PathVariable String franchiseId) {
        return franchiseService.getFranchiseById(franchiseId)
                .map(this::toFranchiseResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // Helper methods for mapping
    private FranchiseResponse toFranchiseResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches().stream()
                        .map(this::toBranchResponse)
                        .toList())
                .createdAt(franchise.getCreatedAt())
                .updatedAt(franchise.getUpdatedAt())
                .build();
    }

    private BranchResponse toBranchResponse(com.franchise.domain.entity.Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts().stream()
                        .map(this::toProductResponse)
                        .toList())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    private ProductResponse toProductResponse(com.franchise.domain.entity.Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

}