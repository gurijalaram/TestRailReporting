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
public class ReportRequest {
    private String externalId;
    private String reportFormat;
    private String reportType;
    private String reportTemplateIdentity;
    private String scopedIdentity;
    private ReportParameters reportParameters;
}
