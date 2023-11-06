package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Schema(location = "ReportTypeResponseSchema.json")
@Data
public class ReportType {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private ReportType response;
}
