package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ScenarioCostingDefaultsResponse.json")
@Data
@JsonRootName("response")
public class ScenarioCostingDefaultsResponse {
    private String materialMode;
    private String materialName;
    private String materialStockName;
    private String materialUtilizationMode;
    private String vpeName;
    private Boolean usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories;
    private String identity;
    private String[] availablePgNames;
    private Boolean machiningModeEnabled;
    private Integer computedBatchSize;
    private Integer batchSize;
    private Boolean manuallyCosted;
    private Integer batchSizeOverride;
    private Boolean defaultToPrimaryVpe;
    private ScenarioKey scenarioKey;
    private String processGroupName;
    private Double productionLife;
    private String compType;
    private Boolean thicknessVisible;
    private Boolean sourceModelVisible;
    private ScenarioDigitalFactoryBean vpeBean;
    private Boolean initialized;
    private Boolean pgEnabled;
    private Boolean supportsMaterials;
    private String machiningMode;
    private Boolean hasTargetFinishMass;
    private ScenarioMaterialBean materialBean;
    private Boolean annualVolumeOverridden;
    private Boolean hasTargetCost;
    private String manualCurrencyCode;
    private String[] availableCurrencyCodes;
    private Boolean compositesFileVisible;
    private Boolean batchSizeOverridden;
    private Boolean manualCurrencyCodeVisible;
    private Boolean stagesAndMaterialsVisible;
    private String[] availableCurrencyVersions;
    private Integer annualVolume;
    private CustomAttributes customAttributes;
    private Boolean cadModelLoaded;
    private String manualCurrencyVersion;
    private Boolean productionLifeOverridden;
    private Integer componentsPerProduct;
    private CustomAttributesResponse customAttributesRequest;
    private List<ScenarioAvailableProcessGroupSelection> availableProcessGroupSelections;
    private List<RoutingNodeOptions> routingNodeOptions;
}
