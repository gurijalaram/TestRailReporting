package com.apriori.acs.entity.response.acs.getsetproductioninfo;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioKey;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetProductionInfoResponse.json")
public class GetProductionInfoResponse {
    private ScenarioKey scenarioKey;
    private String compType;
    private Boolean initialized;
    private List<String> availablePgNames;
    private String processGroupName;
    private Boolean pgEnabled;
    private VpeBean vpeBean;
    private Boolean supportsMaterials;
    private MaterialBean materialBean;
    private Integer annualVolume;
    private Boolean annualVolumeOverridden;
    private Double productionLife;
    private Boolean productionLifeOverridden;
    private Integer batchSizeOverride;
    private Integer computedBatchSize;
    private Boolean batchSizeOverridden;
    private Integer componentsPerProduct;
    private Boolean manuallyCosted;
    private List<String> availableCurrencyCodes;
    private String manualCurrencyCode;
    private List<String> availableCurrencyVersions;
    private String manualCurrencyVersion;
    private String optionalProcessOverride;
    private String machiningMode;
    private Boolean hasTargetCost;
    private Boolean hasTargetFinishMass;
    private Boolean cadModelLoaded;
    private Boolean thicknessVisible;
    private Boolean defaultToPrimaryVpe;
    private Boolean machiningModeEnabled;
    private Boolean sourceModelVisible;
    private Boolean stagesAndMaterialsVisible;
    private Boolean stockFormVisible;
    private List<String> availableStockForms;
    private Boolean xsltFileVisible;
    private Boolean manualCurrencyCodeVisible;
    private List<ProcessGroupItem> availableProcessGroupSelections;
}