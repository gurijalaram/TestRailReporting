package com.apriori.models.response.component;

import com.apriori.models.request.component.RoutingNodeOptions;
import com.apriori.models.response.CustomAttributes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Thumbnail;

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
    private GcdProperties gcdProperties;
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
    private Integer targetMass;


    public static class Threads {
    }

    public static class Tolerances {
    }

    public static class GcdProperties {
    }
}

