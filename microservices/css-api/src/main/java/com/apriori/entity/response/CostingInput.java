package com.apriori.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostingInput {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private CustomAttributes customAttributes;
    private String machiningMode;
    private String materialMode;
    private String materialName;
    private String materialStockName;
    private String processGroupName;
    private Boolean usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories;
    private String vpeName;
    private Double targetCost;
    private List<Object> scenarioCustomAttributes;
    private List<Object> scenarioDesignIssues;
    private ProcessSetupOptions processSetupOptions;
    private Integer productionLife;
    private SecondaryProcesses secondaryProcesses;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private Threads threads;
    private Tolerances tolerances;
    private Thumbnail thumbnail;
    private String materialUtilizationMode;
    private List<RoutingNodeOptions> routingNodeOptions;
    private String twoModelSourceScenarioIdentity;

    public static class ProcessSetupOptions {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SecondaryProcesses {
        @JsonProperty("Other Secondary Processes")
        private List<String> otherSecondaryProcesses;
    }

    public static class SecondaryDigitalFactories {
    }

    public static class Threads {
    }

    public static class Tolerances {
    }


}

