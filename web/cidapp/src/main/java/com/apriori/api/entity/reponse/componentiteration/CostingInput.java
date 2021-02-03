package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CostingInput {

    @JsonProperty("identity")
    private String identity;
    @JsonProperty("annualVolume")
    private Integer annualVolume;
    @JsonProperty("batchSize")
    private Integer batchSize;
    @JsonProperty("customAttributes")
    private CustomAttributes customAttributes;
    @JsonProperty("materialName")
    private String materialName;
    @JsonProperty("processGroupName")
    private String processGroupName;
    @JsonProperty("productionLife")
    private Integer productionLife;
    @JsonProperty("vpeName")
    private String vpeName;

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public CostingInput setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public CostingInput setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    @JsonProperty("batchSize")
    public Integer getBatchSize() {
        return batchSize;
    }

    @JsonProperty("batchSize")
    public CostingInput setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @JsonProperty("customAttributes")
    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    @JsonProperty("customAttributes")
    public CostingInput setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    @JsonProperty("materialName")
    public String getMaterialName() {
        return materialName;
    }

    @JsonProperty("materialName")
    public CostingInput setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    @JsonProperty("processGroupName")
    public String getProcessGroupName() {
        return processGroupName;
    }

    @JsonProperty("processGroupName")
    public CostingInput setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public CostingInput setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    @JsonProperty("vpeName")
    public String getVpeName() {
        return vpeName;
    }

    @JsonProperty("vpeName")
    public CostingInput setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
