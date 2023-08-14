package com.apriori.cir.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonRootName("parameters")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametersRequest {
    private List<ReportParametersRequest> reportParameter;

    public ReportParametersRequest getReportParameterByName(final String name) {
        return reportParameter.stream().filter(report -> name.equals(report.getName()))
            .findFirst().orElse(null);
    }
}
