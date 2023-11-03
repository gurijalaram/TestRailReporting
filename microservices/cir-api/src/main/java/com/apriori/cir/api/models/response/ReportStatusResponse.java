package com.apriori.cir.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Schema(location = "ReportStatusResponse.json")
@Data
public class ReportStatusResponse {
    private String status;
    private String id;
    private String requestId;
    private String reportURI;

    @JsonProperty("exports")
    private List<ExportsStatus> exportsStatuses;
}
