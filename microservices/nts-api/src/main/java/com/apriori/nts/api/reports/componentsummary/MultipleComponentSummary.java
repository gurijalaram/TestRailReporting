package com.apriori.nts.api.reports.componentsummary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleComponentSummary {
    private String title;
    private String rollupName;
    private String scenarioName;
    private String exportDate;
    private String currency;
    private String costMetric;
    private List<MultipleComponentSummaryDetails> multipleComponentSummaryDetails;
}
