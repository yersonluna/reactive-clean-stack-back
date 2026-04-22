package com.franchise.application.service;

import com.franchise.domain.entity.Branch;
import com.franchise.domain.entity.Franchise;
import com.franchise.domain.entity.Product;
import com.franchise.infrastructure.persistence.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("null")
public class FranchiseService {

    private final FranchiseRepository franchiseRepository;

    // RF-01: Agregar Franquicia
public Mono<Franchise> addFranchise(String name) {
    // Creamos el objeto fuera del defer para mayor claridad
    Franchise franchise = Franchise.builder()
            .id(UUID.randomUUID().toString())
            .name(name)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .branches(new ArrayList<>()) // Inicializa la lista para evitar NullPointerException luego
            .build();

    return franchiseRepository.save(franchise)
            .onErrorResume(e -> {
                log.error("Error al persistir la franquicia: {}", e.getMessage());
                return Mono.error(new RuntimeException("No se pudo guardar la franquicia"));
            });
}

    // RF-02: Agregar Sucursal
    public Mono<Franchise> addBranch(String franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch newBranch = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branchName)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    franchise.getBranches().add(newBranch);
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-03: Agregar Producto
    public Mono<Franchise> addProduct(String franchiseId, String branchId, String productName, Long stock) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    Product newProduct = Product.builder()
                            .id(UUID.randomUUID().toString())
                            .name(productName)
                            .stock(stock)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    branch.getProducts().add(newProduct);
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-04: Eliminar Producto
    public Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    boolean removed = branch.getProducts().removeIf(p -> p.getId().equals(productId));
                    if (!removed) {
                        return Mono.error(new RuntimeException("Product not found"));
                    }

                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-05: Modificar Stock
    public Mono<Franchise> updateStock(String franchiseId, String branchId, String productId, Long newStock) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    product.setStock(newStock);
                    product.setUpdatedAt(LocalDateTime.now());
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-06: Top Stock por Sucursal
    public Flux<com.franchise.infrastructure.dto.TopStockResponse> getTopStockByBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .flatMapIterable(franchise -> franchise.getBranches())
                .flatMap(branch -> {
                    if (branch.getProducts().isEmpty()) {
                        return Mono.empty();
                    }
                    Product topProduct = branch.getProducts().stream()
                            .max((p1, p2) -> Long.compare(p1.getStock(), p2.getStock()))
                            .orElseThrow();

                    return Mono.just(com.franchise.infrastructure.dto.TopStockResponse.builder()
                            .branchId(branch.getId())
                            .branchName(branch.getName())
                            .productId(topProduct.getId())
                            .productName(topProduct.getName())
                            .stock(topProduct.getStock())
                            .build());
                });
    }

    // RF-07: Actualización Pro - Renombrar Franquicia
    public Mono<Franchise> renameFranchise(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-07: Actualización Pro - Renombrar Sucursal
    public Mono<Franchise> renameBranch(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    branch.setName(newName);
                    branch.setUpdatedAt(LocalDateTime.now());
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // RF-07: Actualización Pro - Renombrar Producto
    public Mono<Franchise> renameProduct(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    product.setName(newName);
                    product.setUpdatedAt(LocalDateTime.now());
                    franchise.setUpdatedAt(LocalDateTime.now());
                    return franchiseRepository.save(franchise);
                });
    }

    // Utility: Get franchise by ID
    public Mono<Franchise> getFranchiseById(String franchiseId) {
        return franchiseRepository.findById(franchiseId);
    }

    // Utility: Get all franchises
    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }

}