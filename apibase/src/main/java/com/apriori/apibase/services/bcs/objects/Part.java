package com.apriori.apibase.services.bcs.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "CisPartSchema.json")
public class Part {
    private Part response;
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
    private String state;
    private String filename;
    private Integer filesize;
    private String externalId;
    private String batchName;
    private Integer batchSize;
    private Integer annualVolume;
    private String scenarioName;
    private String description;
    private String processGroup;
    private String pinnedRouting;
    private String vpeName;
    private String url;
    private Double productionLife;
    private String materialName;
    private Object udas;
    private String partName;
    private String batchIdentity;
    private String errors;

    public String getErrors() {
        return errors;
    }

    public Part setErrors(String errors) {
        this.errors = errors;
        return this;
    }

    public String getBatchIdentity() {
        return this.batchIdentity;
    }

    public Part setBatchIdentity(String batchIdentity) {
        this.batchIdentity = batchIdentity;
        return this;
    }

    public String getPartName() {
        return this.partName;
    }

    public Part setPartName(String partName) {
        this.partName = partName;
        return this;
    }

    public String getMaterialName() {
        return this.materialName;
    }

    public Part setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public Object getUdas() {
        return this.udas;
    }

    public Part setUdas(Object udas) {
        this.udas = udas;
        return this;
    }

    public Double getProductionLife() {
        return this.productionLife;
    }

    public Part setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public String getPinnedRouting() {
        return this.pinnedRouting;
    }

    public Part setPinnedRouting(String pinnedRouting) {
        this.pinnedRouting = pinnedRouting;
        return this;
    }

    public String getVpeName() {
        return this.vpeName;
    }

    public Part setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public Part setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getProcessGroup() {
        return this.processGroup;
    }

    public Part setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Part setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getScenarioName() {
        return this.scenarioName;
    }

    public Part setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public Part getResponse() {
        return this.response;
    }

    public Part setResponse(Part response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Part setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Part setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Part setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Part setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Part setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public Part setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return this.deploymentIdentity;
    }

    public Part setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getUserIdentity() {
        return this.userIdentity;
    }

    public Part setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getState() {
        return this.state;
    }

    public Part setState(String state) {
        this.state = state;
        return this;
    }

    public String getFilename() {
        return this.filename;
    }

    public Part setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public Integer getFilesize() {
        return this.filesize;
    }

    public Part setFilesize(Integer filesize) {
        this.filesize = filesize;
        return this;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Part setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public Part setBatchName(String batchName) {
        this.batchName = batchName;
        return this;
    }

    public Integer getBatchSize() {
        return this.batchSize;
    }

    public Part setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Integer getAnnualVolume() {
        return this.annualVolume;
    }

    public Part setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }
}
