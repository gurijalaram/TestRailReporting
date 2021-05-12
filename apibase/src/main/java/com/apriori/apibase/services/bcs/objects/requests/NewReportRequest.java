package com.apriori.apibase.services.bcs.objects.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class NewReportRequest {
    private String externalId;
    private String reportFormat;
    private String reportType;
    private Boolean roundToNearest;
    private ArrayList<String> partIdentities;

    public String getExternalId() {
        return this.externalId;
    }

    public NewReportRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getReportFormat() {
        return this.reportFormat;
    }

    public NewReportRequest setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
        return this;
    }

    public String getReportType() {
        return this.reportType;
    }

    public NewReportRequest setReportType(String reportType) {
        this.reportType = reportType;
        return this;
    }

    public Boolean getRoundToNearest() {
        return this.roundToNearest;
    }

    public NewReportRequest setRoundToNearest(Boolean roundToNearest) {
        this.roundToNearest = roundToNearest;
        return this;
    }

    public ArrayList<String> getPartIdentities() {
        return this.partIdentities;
    }

    public NewReportRequest setPartIdentities(ArrayList<String> partIdentities) {
        this.partIdentities = partIdentities;
        return this;
    }

    @JsonIgnore
    public NewReportRequest addPart(String identity) {
        this.partIdentities.add(identity);
        return this;
    }
}
