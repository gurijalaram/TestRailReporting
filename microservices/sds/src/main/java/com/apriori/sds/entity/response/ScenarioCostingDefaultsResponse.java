package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "sds/ScenarioCostingDefaultsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioCostingDefaultsResponse extends JacksonUtil {
    private ScenarioCostingDefaultsResponse response;
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
