package com.franchise.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseResponse {

    private String id;

    private String name;

    private List<BranchResponse> branches;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
