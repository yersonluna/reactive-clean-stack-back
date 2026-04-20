package com.franchise.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    private String id;

    private String name;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
