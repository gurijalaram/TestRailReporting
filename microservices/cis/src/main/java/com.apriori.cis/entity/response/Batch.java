package com.apriori.cis.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "CustomerBatchSchema.json")
public class Batch {
    
    private String identity;
    private String createdBy;
    private String workflowName;
    private String deploymentIdentity;


    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String customerIdentity;
    private String installationIdentity;
    private String state;
    private String externalId;
    private Batch response;
    private String[] parts;
    private String reportIdentity;

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public Batch setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Batch setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
        return this;
    }

    public String getReportIdentity() {
        return this.reportIdentity;
    }

    public Batch setReportIdentity(String reportIdentity) {
        this.reportIdentity = reportIdentity;
        return this;
    }

    public String[] getParts() {
        return this.parts;
    }

    public Batch setParts(String[] parts) {
        this.parts = parts;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Batch setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Batch setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Batch getResponse() {
        return this.response;
    }

    public Batch setResponse(Batch response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Batch setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Batch setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Batch setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public Batch setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return this.installationIdentity;
    }

    public Batch setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getState() {
        return this.state;
    }

    public Batch setState(String state) {
        this.state = state;
        return this;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Batch setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }
}
