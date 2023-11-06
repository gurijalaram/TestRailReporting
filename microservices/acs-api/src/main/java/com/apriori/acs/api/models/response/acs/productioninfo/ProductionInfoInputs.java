package com.apriori.acs.api.models.response.acs.productioninfo;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioKey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductionInfoInputs {
    private ScenarioKey scenarioKey;
    private String compType;
    private String initialized;
    private List<String> availablePgNames;
    private String processGroupName;
    private String pgEnabled;
    private String cadModelLoaded;
    private VpeBean vpeBean;
    private Boolean supportsMaterials;
    private MaterialBean materialBean;
    private Integer annualVolume;
    private Boolean annualVolumeOverriden;
    private Integer productionLife;
    private Boolean productionLifeOverriden;
    private Integer computedBatchSize;
    private Boolean batchSizeOverriden;
    private Integer componentsPerProduct;
    private Boolean manuallyCosted;
    private List<String> availableCurrencyCodes;
    private String manualCurrencyCode;
    private List<String> availableCurrencyVersions;
    private String manualCurrencyVersion;
    private Boolean hasTargetCost;
    private Boolean hasTargetFinishMass;
    private String machiningMode;
    private Boolean thicknessVisible;
    private String compositesFileName;
}
