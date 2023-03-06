package com.apriori.edcapi.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportData {
    private String reportType;
    private String emailAddress;
    private Integer numberOfDaysToReport;
}
