package com.apriori.acs.api.models.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MappingObject {
    private double capitalInvestment;
    private int level;
    private int occurrences;
    private double totalCost;
}
