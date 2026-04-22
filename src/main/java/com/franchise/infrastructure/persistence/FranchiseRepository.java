package com.franchise.infrastructure.persistence;

import com.franchise.domain.entity.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {

    Mono<Franchise> findByName(String name);

    Flux<Franchise> findAll();

}