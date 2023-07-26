package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;

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
