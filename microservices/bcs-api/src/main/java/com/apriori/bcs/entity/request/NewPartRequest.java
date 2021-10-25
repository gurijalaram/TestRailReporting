package com.apriori.bcs.entity.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.primitives.Bytes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NewPartRequest {
    private String filename;
    private Bytes data;
    private String externalId;
    private Object annualVolume;
    private Object batchSize;
    private String description;
    private String materialName;
    private String pinnedRouting;
    private String processGroup;
    private Object productionLife;
    private String scenarioName;
    private String udas;
    private String vpeName;

    @Setter(onMethod_ = {@JsonSetter("UDARegion")})
    @Getter(onMethod_ = {@JsonGetter("UDARegion")})
    private String udaRegion;

    public void setVpeName(String vpeName) {
        if (vpeName == null) {
            this.vpeName = null;
        } else {
            this.vpeName = vpeName;
        }
    }

    public void setMaterialName(String materialName) {
        if (materialName == null) {
            this.materialName = null;
        } else {
            this.materialName = materialName;
        }
    }
}