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
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public void setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
    }

    @JsonProperty("batchSize")
    public Integer getBatchSize() {
        return batchSize;
    }

    @JsonProperty("batchSize")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @JsonProperty("customAttributes")
    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    @JsonProperty("customAttributes")
    public void setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
    }

    @JsonProperty("materialName")
    public String getMaterialName() {
        return materialName;
    }

    @JsonProperty("materialName")
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @JsonProperty("processGroupName")
    public String getProcessGroupName() {
        return processGroupName;
    }

    @JsonProperty("processGroupName")
    public void setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public void setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
    }

    @JsonProperty("vpeName")
    public String getVpeName() {
        return vpeName;
    }

    @JsonProperty("vpeName")
    public void setVpeName(String vpeName) {
        this.vpeName = vpeName;
    }
}
