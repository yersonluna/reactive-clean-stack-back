package com.franchise.application.service;

import com.franchise.domain.entity.Branch;
import com.franchise.domain.entity.Franchise;
import com.franchise.domain.entity.Product;
import com.franchise.infrastructure.persistence.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private FranchiseService franchiseService;

    @BeforeEach
    void setUp() {
        franchiseService = new FranchiseService(franchiseRepository);
    }

    @Test
    void testAddFranchise() {
        // Arrange
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.defer(() -> Mono.just(Franchise.builder()
                        .id("1")
                        .name("Test Franchise")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())));

        // Act & Assert
        StepVerifier.create(franchiseService.addFranchise("Test Franchise"))
                .expectNextMatches(f -> f.getName().equals("Test Franchise"))
                .verifyComplete();
    }

    @Test
    void testAddBranch() {
        // Arrange
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(franchiseRepository.findById("1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(franchiseService.addBranch("1", "Test Branch"))
                .expectNextMatches(f -> f.getBranches().size() == 1)
                .verifyComplete();
    }

    @Test
    void testAddProduct() {
        // Arrange
        Branch branch = Branch.builder()
                .id("b1")
                .name("Test Branch")
                .products(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        franchise.getBranches().add(branch);

        when(franchiseRepository.findById("1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(franchiseService.addProduct("1", "b1", "Test Product", 100L))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().size() == 1)
                .verifyComplete();
    }

    @Test
    void testUpdateStock() {
        // Arrange
        Product product = Product.builder()
                .id("p1")
                .name("Test Product")
                .stock(100L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Branch branch = Branch.builder()
                .id("b1")
                .name("Test Branch")
                .products(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        branch.getProducts().add(product);

        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        franchise.getBranches().add(branch);

        when(franchiseRepository.findById("1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(franchiseService.updateStock("1", "b1", "p1", 200L))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().get(0).getStock() == 200L)
                .verifyComplete();
    }

    @Test
    void testGetTopStockByBranch() {
        // Arrange
        Product product1 = Product.builder()
                .id("p1")
                .name("Product 1")
                .stock(100L)
                .build();

        Product product2 = Product.builder()
                .id("p2")
                .name("Product 2")
                .stock(200L)
                .build();

        Branch branch = Branch.builder()
                .id("b1")
                .name("Test Branch")
                .products(new ArrayList<>())
                .build();
        branch.getProducts().add(product1);
        branch.getProducts().add(product2);

        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .branches(new ArrayList<>())
                .build();
        franchise.getBranches().add(branch);

        when(franchiseRepository.findById("1")).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(franchiseService.getTopStockByBranch("1"))
                .expectNextMatches(t -> t.getStock() == 200L && t.getProductName().equals("Product 2"))
                .verifyComplete();
    }

    @Test
    void testRenameFranchise() {
        // Arrange
        Franchise franchise = Franchise.builder()
                .id("1")
                .name("Old Name")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(franchiseRepository.findById("1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(franchiseService.renameFranchise("1", "New Name"))
                .expectNextMatches(f -> f.getName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    void testGetAllFranchises() {
        // Arrange
        Franchise franchise1 = Franchise.builder()
                .id("1")
                .name("Franchise 1")
                .build();

        Franchise franchise2 = Franchise.builder()
                .id("2")
                .name("Franchise 2")
                .build();

        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise1, franchise2));

        // Act & Assert
        StepVerifier.create(franchiseService.getAllFranchises())
                .expectNext(franchise1)
                .expectNext(franchise2)
                .verifyComplete();
    }

}
