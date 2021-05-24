package com.apriori.cidapp.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostRequest {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private Object customAttributes;
    private String materialName;
    private String processGroupName;
    private Double productionLife;
    private String vpeName;

    public String getIdentity() {
        return identity;
    }

    public CostRequest setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public CostRequest setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public CostRequest setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Object getCustomAttributes() {
        return customAttributes;
    }

    public CostRequest setCustomAttributes(Object customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public CostRequest setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public CostRequest setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public Double getProductionLife() {
        return productionLife;
    }

    public CostRequest setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public CostRequest setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
