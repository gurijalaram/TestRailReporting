package com.apriori.cirapi.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
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
