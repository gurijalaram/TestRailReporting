package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "ReportTypeResponseSchema.json")
public class ReportType {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private ReportType response;

    public ReportType getResponse() {
        return this.response;
    }

    public ReportType setResponse(ReportType response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public ReportType setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ReportType setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public ReportType setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ReportType setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public ReportType setDescription(String description) {
        this.description = description;
        return this;
    }
}
