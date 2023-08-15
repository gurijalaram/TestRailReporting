package com.apriori.bcs.models.request.parts;

import com.google.common.primitives.Bytes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPartRequest {
    private String filename;
    private Bytes data;
    private String externalId;
    private Integer annualVolume;
    private Integer batchSize;
    private String description;
    private String material;
    private String pinnedRouting;
    private String processGroup;
    private Object productionLife;
    private String scenarioName;
    private String udas;
    private String digitalFactory;
    private String generateWatchPointReport;
    private String fileData;
    private Integer years;
    private String secondaryDigitalFactory;

    public void setDigitalFactory(String digitalFactory) {
        if (digitalFactory == null) {
            this.digitalFactory = null;
        } else {
            this.digitalFactory = digitalFactory;
        }
    }

    public void setMaterial(String material) {
        if (material == null) {
            this.material = null;
        } else {
            this.material = material;
        }
    }
}

