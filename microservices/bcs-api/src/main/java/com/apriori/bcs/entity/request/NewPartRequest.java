package com.apriori.bcs.entity.request;

import com.google.common.primitives.Bytes;
import lombok.Data;

@Data
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
