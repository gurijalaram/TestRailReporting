package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

import java.util.List;

@Schema(location = "sds/ScenarioCostingDefaultsResponse.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioCostingDefaultsResponse {
    private String[] availablePgNames;
    private Boolean machiningModeEnabled;
    private Integer computedBatchSize;
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
    private Boolean cadModelLoaded;
    private String manualCurrencyVersion;
    private Boolean productionLifeOverridden;
    private Integer componentsPerProduct;
    private List<ScenarioAvailableProcessGroupSelection> availableProcessGroupSelections;
}
