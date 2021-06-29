package com.apriori.bcs.entity.response;

import lombok.Data;

@Data
public class ReportParametersResponse {
    private boolean roundToDollar;
    private String currencyCode;
    private String partNumber;
    private String exportSetName;
    private String scenarioName;
    private String sortMetric;
    private String massMetric;
    private String costMetric;
    private String[] riskRating;
    private String rollup;
}

