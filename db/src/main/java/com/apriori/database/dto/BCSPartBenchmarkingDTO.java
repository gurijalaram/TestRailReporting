package com.apriori.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BCSPartBenchmarkingDTO {

    private String identity;
    private String partName;
    private String batchIdentity;
    private String costingResults;
    private String errorMessage;
    private String state;
    private LocalDateTime startTime;
    private Long costingDuration;
    private String processGroup;
    private String materialName;
    private String filename;
    private Integer batchSize;
    private Integer annualVolume;

    public BCSPartBenchmarkingDTO setCostingDuration(LocalDateTime updatedTime) {
        this.costingDuration = ChronoUnit.SECONDS.between(startTime, updatedTime);
        return this;
    }
}
