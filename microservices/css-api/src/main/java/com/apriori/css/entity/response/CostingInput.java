package com.apriori.css.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostingInput {
    private String identity;
    private Integer annualVolume;
    private CustomAttributes customAttributes;
    private String machiningMode;
    private String materialMode;
    private String processGroupName;
    private String vpeName;
    private ProcessSetupOptions processSetupOptions;
    private Integer productionLife;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private Threads threads;
    private Tolerances tolerances;

    public static class CustomAttributes {
    }

    public static class ProcessSetupOptions {
    }

    public static class SecondaryProcesses {
    }

    public static class SecondaryDigitalFactories {
    }

    public static class Threads {
    }

    public static class Tolerances {
    }
}

