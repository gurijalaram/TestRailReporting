package com.apriori.acs.api.models.response.workorders.bomloader;

import lombok.Data;

@Data
public class BomLoaderMapping {
    private Double capitalInvestment;
    private Integer level;
    private Integer occurrences;
    private Double totalCost;
}
