package com.apriori.shared.util.file.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartData {

    private String filename;
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
    private File file;
    private Double years;
    private String secondaryDigitalFactory;
    private String plmPartNumber;
    private String plmMapped;
    // benchmark metrics
    private String identity;
    private String partName;
    private String batchIdentity;
    private String costingResults;
    private String errorMessage;
    private String state;
    private LocalDateTime startTime;
    private Long costingDuration;

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

    public PartData setCostingDuration(LocalDateTime updatedTime) {
        this.costingDuration = ChronoUnit.SECONDS.between(startTime, updatedTime);
        return this;
    }
}