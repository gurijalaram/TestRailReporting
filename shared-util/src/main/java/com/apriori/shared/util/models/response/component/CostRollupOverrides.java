package com.apriori.shared.util.models.response.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostRollupOverrides {
    Double amortizedBatchSetup;
    Double fixtureCostPerPart;
    Double piecePartCost;
    Double programmingCostPerPart;
    Double toolingCostPerPart;
    Double totalAmortizedInvestment;
    Double totalCapitalInvestment;


}
