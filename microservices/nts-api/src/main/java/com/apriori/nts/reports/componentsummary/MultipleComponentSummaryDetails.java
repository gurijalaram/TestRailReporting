package com.apriori.nts.reports.componentsummary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleComponentSummaryDetails {
    private String partNumber;
    private String scenario;
    private String partType;
}
