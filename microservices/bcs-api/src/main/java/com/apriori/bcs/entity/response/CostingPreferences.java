package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CostingPreferencesSchema.json")
public class CostingPreferences {
    private Integer annualVolume;
    private String batchMode;
    private Boolean cadTolerances;
    private Double cadToleranceReplacement;
    private Double minCadToleranceThreshold;
    private Boolean multiBodyKeepFreeBodies;
    private Boolean multiBodyIgnoreMissingComponents;
    private Boolean multiBodyPreserveCad;
    private String processGroup;
    private Double productionLife;
    private String scenarioName;
    private List<Tolerance> tolerances;
    private Boolean useCadToleranceThreshold;
    private Boolean useVpeForAllProcesses;
    private String vpeName;
}