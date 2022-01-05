package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.css.entity.response.Thumbnail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostingInput {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private CustomAttributes customAttributes;
    private String materialName;
    private String machiningMode;
    private String materialMode;
    private String processGroupName;
    private Double productionLife;
    private String vpeName;
    private ProcessSetupOptions processSetupOptions;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private Threads threads;
    private Tolerances tolerances;
    private Thumbnail thumbnail;
    private String materialUtilizationMode;
    private Boolean usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories;

    static class CustomAttributes {
    }

    static class ProcessSetupOptions {
    }

    static class SecondaryProcesses {
    }

    static class SecondaryDigitalFactories {
    }

    static class Threads {
    }

    static class Tolerances {
    }
}
