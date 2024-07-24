package com.apriori.acs.api.models.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MappingObject {
    private Double capitalInvestment;
    private Integer level;
    private Integer occurrences;
    private Double totalCost;
}
