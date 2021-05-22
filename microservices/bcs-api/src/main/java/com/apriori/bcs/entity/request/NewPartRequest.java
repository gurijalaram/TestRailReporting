package com.apriori.bcs.entity.request;

import com.google.common.primitives.Bytes;

public class NewPartRequest {
    private String filename;
    private Bytes data;
    private String externalId;
    private Integer annualVolume;
    private Integer batchSize;
    private String description;
    private String materialName;
    private String pinnedRouting;
    private String processGroup;
    private Double productionLife;
    private String scenarioName;
    private String udas;
    private String vpeName;

    public String getFilename() {
        return filename;
    }

    public NewPartRequest setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public Bytes getData() {
        return data;
    }

    public NewPartRequest setData(Bytes data) {
        this.data = data;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public NewPartRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public NewPartRequest setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public NewPartRequest setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NewPartRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public NewPartRequest setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public String getPinnedRouting() {
        return pinnedRouting;
    }

    public NewPartRequest setPinnedRouting(String pinnedRouting) {
        this.pinnedRouting = pinnedRouting;
        return this;
    }

    public String getProcessGroup() {
        return processGroup;
    }

    public NewPartRequest setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
        return this;
    }

    public Double getProductionLife() {
        return productionLife;
    }

    public NewPartRequest setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public NewPartRequest setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getUdas() {
        return udas;
    }

    public NewPartRequest setUdas(String udas) {
        this.udas = udas;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public NewPartRequest setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
