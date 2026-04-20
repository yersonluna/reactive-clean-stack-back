package com.franchise.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private String id;

    private String name;

    private Long stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
