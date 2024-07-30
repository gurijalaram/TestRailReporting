package com.apriori.bcs.api.models.request.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportParameters {
    private String roundToDollar;
    private String currencyCode;
    private String[] riskRating;
    private String costMetric;
    private String massMetric;
    private String sortMetric;
}
