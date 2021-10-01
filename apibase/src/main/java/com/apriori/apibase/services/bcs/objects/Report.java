package com.apriori.apibase.services.bcs.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "CisReportSchema.json")
public class Report { 
    private String identity;
    private String createdBy;
    private String updatedBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;

    private String customerIdentity;
    private String deploymentIdentity;
    private String userIdentity;
    private String reportType;
    private String roundToNearest;
    private String externalId;
    private String state;
    private String reportFormat;
    private String[] parts;
    private Report response;
    private String fileName;
    private String contentType;
    private String batchIdentity;

    public String getBatchIdentity() {
        return batchIdentity;
    }

    public Report setBatchIdentity(String batchIdentity) {
        this.batchIdentity = batchIdentity;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Report setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Report setFileName(String filename) {
        this.fileName = filename;
        return this;
    }

    public Report getResponse() {
        return this.response;
    }

    public Report setResponse(Report response) {
        this.response = response;
        return this;
    }


    public String getIdentity() {
        return this.identity;
    }

    public Report setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Report setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Report setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Report setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Report setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public Report setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return this.deploymentIdentity;
    }

    public Report setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getUserIdentity() {
        return this.userIdentity;
    }

    public Report setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getReportType() {
        return this.reportType;
    }

    public Report setReportType(String reportType) {
        this.reportType = reportType;
        return this;
    }

    public String getRoundToNearest() {
        return this.roundToNearest;
    }

    public Report setRoundToNearest(String roundToNearest) {
        this.roundToNearest = roundToNearest;
        return this;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Report setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getState() {
        return this.state;
    }

    public Report setState(String state) {
        this.state = state;
        return this;
    }

    public String getReportFormat() {
        return this.reportFormat;
    }

    public Report setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
        return this;
    }

    public String[] getParts() {
        return this.parts;
    }

    public Report setParts(String[] parts) {
        this.parts = parts;
        return this;
    }
}
