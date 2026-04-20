package com.franchise.infrastructure.controller;

import com.franchise.application.service.FranchiseService;
import com.franchise.domain.entity.Branch;
import com.franchise.domain.entity.Franchise;
import com.franchise.domain.entity.Product;
import com.franchise.infrastructure.dto.FranchiseRequest;
import com.franchise.infrastructure.dto.ProductRequest;
import com.franchise.infrastructure.dto.BranchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranchiseService franchiseService;

    private Franchise mockFranchise;

    @BeforeEach
    void setUp() {
        mockFranchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddFranchise() {
        // Arrange
        FranchiseRequest request = new FranchiseRequest();
        request.setName("Test Franchise");

        when(franchiseService.addFranchise("Test Franchise"))
                .thenReturn(Mono.just(mockFranchise));

        // Act & Assert
        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("Test Franchise");
    }

    @Test
    void testGetAllFranchises() {
        // Arrange
        when(franchiseService.getAllFranchises())
                .thenReturn(Flux.just(mockFranchise));

        // Act & Assert
        webTestClient.get()
                .uri("/api/franchises")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class)
                .hasSize(1);
    }

    @Test
    void testGetFranchiseById() {
        // Arrange
        when(franchiseService.getFranchiseById("1"))
                .thenReturn(Mono.just(mockFranchise));

        // Act & Assert
        webTestClient.get()
                .uri("/api/franchises/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("Test Franchise");
    }

    @Test
    void testAddBranch() {
        // Arrange
        BranchRequest request = new BranchRequest();
        request.setName("Test Branch");

        when(franchiseService.addBranch("1", "Test Branch"))
                .thenReturn(Mono.just(mockFranchise));

        // Act & Assert
        webTestClient.post()
                .uri("/api/franchises/1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testAddProduct() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setStock(100L);

        when(franchiseService.addProduct("1", "b1", "Test Product", 100L))
                .thenReturn(Mono.just(mockFranchise));

        // Act & Assert
        webTestClient.post()
                .uri("/api/franchises/1/branches/b1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        when(franchiseService.deleteProduct("1", "b1", "p1"))
                .thenReturn(Mono.just(mockFranchise));

        // Act & Assert
        webTestClient.delete()
                .uri("/api/franchises/1/branches/b1/products/p1")
                .exchange()
                .expectStatus().isNoContent();
    }

}
