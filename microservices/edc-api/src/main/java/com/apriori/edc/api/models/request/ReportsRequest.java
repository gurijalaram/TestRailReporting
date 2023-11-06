package com.apriori.edc.api.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportsRequest {
    private ReportData report;
}
