package com.apriori.report.api.models;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Schema(location = "ExecuteResponse.json")
@Data
public class Report {
    private String executionId;
    private String status;
    private String url;
}
