package com.franchise.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseRequest {

    private String name;

}
