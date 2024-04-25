package com.apriori.shared.util.models.response.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostRollupOverrides {
    public Double amortizedBatchSetup;
    public Double fixtureCostPerPart;
    public Double piecePartCost;
    public Double programmingCostPerPart;
    public Double toolingCostPerPart;
    public Double totalAmortizedInvestment;
    public Double totalCapitalInvestment;
}
