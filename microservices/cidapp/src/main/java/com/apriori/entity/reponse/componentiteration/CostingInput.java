package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostingInput {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private CustomAttributes customAttributes;
    private String materialName;
    private String processGroupName;
    private Double productionLife;
    private String vpeName;

    public String getIdentity() {
        return identity;
    }

    public CostingInput setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public CostingInput setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public CostingInput setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    public CostingInput setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public CostingInput setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public CostingInput setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public Double getProductionLife() {
        return productionLife;
    }

    public CostingInput setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public CostingInput setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
