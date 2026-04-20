package com.franchise.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopStockResponse {

    private String branchId;

    private String branchName;

    private String productId;

    private String productName;

    private Long stock;

}
