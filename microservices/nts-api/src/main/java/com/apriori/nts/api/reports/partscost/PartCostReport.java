package com.apriori.nts.api.reports.partscost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartCostReport {

    private String title;
    private String partNumber;
    private String scenario;
}
