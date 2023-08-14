package com.apriori.bcs.models.request.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String externalId;
    private String reportFormat;
    private String reportType;
    private String reportTemplateIdentity;
    private String scopedIdentity;
    private ReportParameters reportParameters;
}
