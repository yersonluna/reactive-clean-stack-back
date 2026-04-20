package com.franchise.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "franchises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Franchise {

    @Id
    private String id;

    private String name;

    @Builder.Default
    private List<Branch> branches = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
