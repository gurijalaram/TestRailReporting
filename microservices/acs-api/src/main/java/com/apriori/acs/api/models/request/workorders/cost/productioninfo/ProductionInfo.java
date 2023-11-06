package com.apriori.acs.api.models.request.workorders.cost.productioninfo;

import com.apriori.shared.util.annotations.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(location = "workorders/ProductionCostResponse.json")
public class ProductionInfo {
    private ProductionInfoScenario scenarioKey;
    private String compType;
    private Boolean initialized;
    private List<String> availablePgNames = null;
    private String processGroupName;
    private Boolean pgEnabled;
    private Boolean cadModelLoaded;
    private ProductionInfoVpe vpeBean;
    private Boolean supportsMaterials;
    private ProductionInfoMaterial materialBean;
    private Integer annualVolume;
    private Boolean annualVolumeOverridden;
    private Integer productionLife;
    private Boolean productionLifeOverridden;
    private Integer computedBatchSize;
    private Boolean batchSizeOverridden;
    private Integer componentsPerProduct;
    private Boolean manuallyCosted;
    private List<String> availableCurrencyCodes = null;
    private String manualCurrencyCode;
    private List<String> availableCurrencyVersions = null;
    private String manualCurrencyVersion;
    private Boolean hasTargetCost;
    private Boolean hasTargetFinishMass;
    private String machiningMode;
    private Boolean thicknessVisible;
    private String compositesFileName;
}