package com.apriori.bcs.entity.request;

import lombok.Data;

@Data
public class ReportParameters {
    private boolean roundToDollar;
    private String currencyCode;
}
