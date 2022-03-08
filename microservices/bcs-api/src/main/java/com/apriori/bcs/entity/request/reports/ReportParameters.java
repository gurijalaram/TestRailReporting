package com.apriori.bcs.entity.request.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportParameters {
    private String roundToDollar;
    private String currencyCode;
}
