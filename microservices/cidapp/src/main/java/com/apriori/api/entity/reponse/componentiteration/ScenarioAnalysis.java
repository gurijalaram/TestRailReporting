package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

public class ScenarioAnalysis {

    @JsonProperty("addFacilityCostToDirOverhead")
    private Boolean addFacilityCostToDirOverhead;
    @JsonProperty("additionalAmortizedInvestment")
    private Integer additionalAmortizedInvestment;
    @JsonProperty("additionalDirectCosts")
    private Integer additionalDirectCosts;
    @JsonProperty("amortizedInvestment")
    private Integer amortizedInvestment;
    @JsonProperty("annualCost")
    private Integer annualCost;
    @JsonProperty("annualEarnedMachineHours")
    private Integer annualEarnedMachineHours;
    @JsonProperty("annualMaintenanceFactor")
    private Integer annualMaintenanceFactor;
    @JsonProperty("annualVolume")
    private Integer annualVolume;
    @JsonProperty("batchCost")
    private Integer batchCost;
    @JsonProperty("batchSetupTime")
    private Integer batchSetupTime;
    @JsonProperty("batchSize")
    private Integer batchSize;
    @JsonProperty("cadSerLength")
    private Integer cadSerLength;
    @JsonProperty("cadSerWidth")
    private Integer cadSerWidth;
    @JsonProperty("capitalInvestment")
    private Integer capitalInvestment;
    @JsonProperty("costingStatus")
    private String costingStatus;
    @JsonProperty("createdAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdByName")
    private String createdByName;
    @JsonProperty("cycleTime")
    private Integer cycleTime;
    @JsonProperty("deletedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    @JsonProperty("deletedBy")
    private String deletedBy;
    @JsonProperty("deletedByName")
    private String deletedByName;
    @JsonProperty("depreciationCost")
    private Integer depreciationCost;
    @JsonProperty("dfmRisk")
    private Integer dfmRisk;
    @JsonProperty("directOverheadCost")
    private Integer directOverheadCost;
    @JsonProperty("directOverheadRate")
    private Integer directOverheadRate;
    @JsonProperty("dtcMessagesCount")
    private Integer dtcMessagesCount;
    @JsonProperty("elapsedTime")
    private Integer elapsedTime;
    @JsonProperty("electricityRate")
    private Integer electricityRate;
    @JsonProperty("energyCost")
    private Integer energyCost;
    @JsonProperty("engineeringDepartmentCost")
    private Integer engineeringDepartmentCost;
    @JsonProperty("engineeringSupportAllocation")
    private Integer engineeringSupportAllocation;
    @JsonProperty("engineeringSupportRate")
    private Integer engineeringSupportRate;
    @JsonProperty("expendableToolingCost")
    private Integer expendableToolingCost;
    @JsonProperty("expendableToolingCostPerCore")
    private Integer expendableToolingCostPerCore;
    @JsonProperty("expendableToolingCostPerPart")
    private Integer expendableToolingCostPerPart;
    @JsonProperty("extraCosts")
    private Integer extraCosts;
    @JsonProperty("facilityDirectOverheadCost")
    private Integer facilityDirectOverheadCost;
    @JsonProperty("facilityElectricityCost")
    private Integer facilityElectricityCost;
    @JsonProperty("facilityElectricityFactor")
    private Integer facilityElectricityFactor;
    @JsonProperty("facilityHeatAndGasCost")
    private Integer facilityHeatAndGasCost;
    @JsonProperty("facilityHeatAndGasFactor")
    private Integer facilityHeatAndGasFactor;
    @JsonProperty("facilityIndirectOverheadCost")
    private Integer facilityIndirectOverheadCost;
    @JsonProperty("facilityWaterCost")
    private Integer facilityWaterCost;
    @JsonProperty("facilityWaterFactor")
    private Integer facilityWaterFactor;
    @JsonProperty("failedGcdsCount")
    private Integer failedGcdsCount;
    @JsonProperty("failuresWarningsCount")
    private Integer failuresWarningsCount;
    @JsonProperty("finalYield")
    private Integer finalYield;
    @JsonProperty("finishMass")
    private Integer finishMass;
    @JsonProperty("fireInsuranceCost")
    private Integer fireInsuranceCost;
    @JsonProperty("fireInsuranceFactor")
    private Integer fireInsuranceFactor;
    @JsonProperty("fixtureCost")
    private Integer fixtureCost;
    @JsonProperty("fixtureCostPerPart")
    private Integer fixtureCostPerPart;
    @JsonProperty("footprintAllowanceFactor")
    private Integer footprintAllowanceFactor;
    @JsonProperty("fullyBurdenedCost")
    private Integer fullyBurdenedCost;
    @JsonProperty("gasRate")
    private Integer gasRate;
    @JsonProperty("gcdWithTolerancesCount")
    private Integer gcdWithTolerancesCount;
    @JsonProperty("goodPartYield")
    private Integer goodPartYield;
    @JsonProperty("hardToolingCost")
    private Integer hardToolingCost;
    @JsonProperty("hardToolingCostPerPart")
    private Integer hardToolingCostPerPart;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("hoursPerShift")
    private Integer hoursPerShift;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("imputedInterestCost")
    private Integer imputedInterestCost;
    @JsonProperty("imputedInterestRate")
    private Integer imputedInterestRate;
    @JsonProperty("indirectOverheadPercent")
    private Integer indirectOverheadPercent;
    @JsonProperty("indirectOverheadRate")
    private Integer indirectOverheadRate;
    @JsonProperty("installationCost")
    private Integer installationCost;
    @JsonProperty("installationFactor")
    private Integer installationFactor;
    @JsonProperty("insuranceCost")
    private Integer insuranceCost;
    @JsonProperty("laborCost")
    private Integer laborCost;
    @JsonProperty("laborRate")
    private Integer laborRate;
    @JsonProperty("laborTime")
    private Integer laborTime;
    @JsonProperty("lastCosted")
    private String lastCosted;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("liabilityInsuranceCost")
    private Integer liabilityInsuranceCost;
    @JsonProperty("liabilityInsuranceFactor")
    private Integer liabilityInsuranceFactor;
    @JsonProperty("lifetimeCost")
    private Integer lifetimeCost;
    @JsonProperty("logisticsCost")
    private Integer logisticsCost;
    @JsonProperty("lossInsuranceCost")
    private Integer lossInsuranceCost;
    @JsonProperty("lossInsuranceFactor")
    private Integer lossInsuranceFactor;
    @JsonProperty("machineLaborRateAdjustmentFactor")
    private Integer machineLaborRateAdjustmentFactor;
    @JsonProperty("machineLength")
    private Integer machineLength;
    @JsonProperty("machineLife")
    private Integer machineLife;
    @JsonProperty("machinePower")
    private Integer machinePower;
    @JsonProperty("machinePrice")
    private Integer machinePrice;
    @JsonProperty("machineUptime")
    private Integer machineUptime;
    @JsonProperty("machineWidth")
    private Integer machineWidth;
    @JsonProperty("maintenanceCost")
    private Integer maintenanceCost;
    @JsonProperty("maintenanceDepartmentCost")
    private Integer maintenanceDepartmentCost;
    @JsonProperty("maintenanceSupportAllocation")
    private Integer maintenanceSupportAllocation;
    @JsonProperty("maintenanceSupportRate")
    private Integer maintenanceSupportRate;
    @JsonProperty("margin")
    private Integer margin;
    @JsonProperty("marginPercent")
    private Integer marginPercent;
    @JsonProperty("materialCost")
    private Integer materialCost;
    @JsonProperty("materialName")
    private String materialName;
    @JsonProperty("materialOverheadCost")
    private Integer materialOverheadCost;
    @JsonProperty("materialOverheadPercent")
    private Integer materialOverheadPercent;
    @JsonProperty("materialStockFormName")
    private String materialStockFormName;
    @JsonProperty("materialStockName")
    private String materialStockName;
    @JsonProperty("materialUnitCost")
    private Integer materialUnitCost;
    @JsonProperty("materialYield")
    private Integer materialYield;
    @JsonProperty("nonProductionFootprintFactor")
    private Integer nonProductionFootprintFactor;
    @JsonProperty("notSupportedGcdsCount")
    private Integer notSupportedGcdsCount;
    @JsonProperty("numOperators")
    private Integer numOperators;
    @JsonProperty("numPartsPerSheet")
    private Integer numPartsPerSheet;
    @JsonProperty("numScrapParts")
    private Integer numScrapParts;
    @JsonProperty("numScrapPartsDownStream")
    private Integer numScrapPartsDownStream;
    @JsonProperty("numberOfParts")
    private Integer numberOfParts;
    @JsonProperty("otherDirectCosts")
    private Integer otherDirectCosts;
    @JsonProperty("overheadRate")
    private Integer overheadRate;
    @JsonProperty("partsPerHour")
    private Integer partsPerHour;
    @JsonProperty("periodOverhead")
    private Integer periodOverhead;
    @JsonProperty("pieceAndPeriod")
    private Integer pieceAndPeriod;
    @JsonProperty("pieceCost")
    private Integer pieceCost;
    @JsonProperty("plantLaborRateAdjustmentFactor")
    private Integer plantLaborRateAdjustmentFactor;
    @JsonProperty("processRoutingName")
    private String processRoutingName;
    @JsonProperty("productionDaysPerYear")
    private Integer productionDaysPerYear;
    @JsonProperty("productionLife")
    private Integer productionLife;
    @JsonProperty("programmingCost")
    private Integer programmingCost;
    @JsonProperty("programmingCostPerPart")
    private Integer programmingCostPerPart;
    @JsonProperty("purchasingDepartmentCost")
    private Integer purchasingDepartmentCost;
    @JsonProperty("purchasingPowerFactor")
    private Integer purchasingPowerFactor;
    @JsonProperty("purchasingSupportAllocation")
    private Integer purchasingSupportAllocation;
    @JsonProperty("purchasingSupportRate")
    private Integer purchasingSupportRate;
    @JsonProperty("qualityDepartmentCost")
    private Integer qualityDepartmentCost;
    @JsonProperty("qualitySupportAllocation")
    private Integer qualitySupportAllocation;
    @JsonProperty("qualitySupportRate")
    private Integer qualitySupportRate;
    @JsonProperty("rentCost")
    private Integer rentCost;
    @JsonProperty("rentRate")
    private Integer rentRate;
    @JsonProperty("roughLength")
    private Integer roughLength;
    @JsonProperty("roughMass")
    private Integer roughMass;
    @JsonProperty("salvageValue")
    private Integer salvageValue;
    @JsonProperty("salvageValueFactor")
    private Integer salvageValueFactor;
    @JsonProperty("scrapMass")
    private Integer scrapMass;
    @JsonProperty("scrapPartCredit")
    private Integer scrapPartCredit;
    @JsonProperty("setupCostPerCore")
    private Integer setupCostPerCore;
    @JsonProperty("setupCostPerPart")
    private Integer setupCostPerPart;
    @JsonProperty("sgaCost")
    private Integer sgaCost;
    @JsonProperty("sgaPercent")
    private Integer sgaPercent;
    @JsonProperty("shiftsPerDay")
    private Integer shiftsPerDay;
    @JsonProperty("skillLevel")
    private Integer skillLevel;
    @JsonProperty("stockPropertyHeight")
    private Integer stockPropertyHeight;
    @JsonProperty("stockPropertyInsideDia")
    private Integer stockPropertyInsideDia;
    @JsonProperty("stockPropertyLength")
    private Integer stockPropertyLength;
    @JsonProperty("stockPropertyOutsideDia")
    private Integer stockPropertyOutsideDia;
    @JsonProperty("stockPropertyThickness")
    private Integer stockPropertyThickness;
    @JsonProperty("stockPropertyWallThickness")
    private Integer stockPropertyWallThickness;
    @JsonProperty("stockPropertyWidth")
    private Integer stockPropertyWidth;
    @JsonProperty("stripNestingPitch")
    private Integer stripNestingPitch;
    @JsonProperty("suppliesCost")
    private Integer suppliesCost;
    @JsonProperty("supportAllocation")
    private Integer supportAllocation;
    @JsonProperty("supportServicesCost")
    private Integer supportServicesCost;
    @JsonProperty("toolCribDepartmentCost")
    private Integer toolCribDepartmentCost;
    @JsonProperty("toolCribSupportAllocation")
    private Integer toolCribSupportAllocation;
    @JsonProperty("toolCribSupportRate")
    private Integer toolCribSupportRate;
    @JsonProperty("toolingCostPerPart")
    private Integer toolingCostPerPart;
    @JsonProperty("totalCost")
    private Integer totalCost;
    @JsonProperty("totalMachineCost")
    private Integer totalMachineCost;
    @JsonProperty("totalProductionVolume")
    private Integer totalProductionVolume;
    @JsonProperty("updatedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("updatedByName")
    private String updatedByName;
    @JsonProperty("useComputedOverheadRate")
    private Boolean useComputedOverheadRate;
    @JsonProperty("useIndirectOverheadPercentage")
    private Boolean useIndirectOverheadPercentage;
    @JsonProperty("utilitiesCost")
    private Integer utilitiesCost;
    @JsonProperty("utilization")
    private Integer utilization;
    @JsonProperty("utilizationWithAddendum")
    private Integer utilizationWithAddendum;
    @JsonProperty("utilizationWithoutAddendum")
    private Integer utilizationWithoutAddendum;
    @JsonProperty("virtualMaterialStockName")
    private String virtualMaterialStockName;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("workCenterCapacity")
    private Integer workCenterCapacity;
    @JsonProperty("workCenterFootprint")
    private Integer workCenterFootprint;

    @JsonProperty("addFacilityCostToDirOverhead")
    public Boolean getAddFacilityCostToDirOverhead() {
        return addFacilityCostToDirOverhead;
    }

    @JsonProperty("addFacilityCostToDirOverhead")
    public ScenarioAnalysis setAddFacilityCostToDirOverhead(Boolean addFacilityCostToDirOverhead) {
        this.addFacilityCostToDirOverhead = addFacilityCostToDirOverhead;
        return this;
    }

    @JsonProperty("additionalAmortizedInvestment")
    public Integer getAdditionalAmortizedInvestment() {
        return additionalAmortizedInvestment;
    }

    @JsonProperty("additionalAmortizedInvestment")
    public ScenarioAnalysis setAdditionalAmortizedInvestment(Integer additionalAmortizedInvestment) {
        this.additionalAmortizedInvestment = additionalAmortizedInvestment;
        return this;
    }

    @JsonProperty("additionalDirectCosts")
    public Integer getAdditionalDirectCosts() {
        return additionalDirectCosts;
    }

    @JsonProperty("additionalDirectCosts")
    public ScenarioAnalysis setAdditionalDirectCosts(Integer additionalDirectCosts) {
        this.additionalDirectCosts = additionalDirectCosts;
        return this;
    }

    @JsonProperty("amortizedInvestment")
    public Integer getAmortizedInvestment() {
        return amortizedInvestment;
    }

    @JsonProperty("amortizedInvestment")
    public ScenarioAnalysis setAmortizedInvestment(Integer amortizedInvestment) {
        this.amortizedInvestment = amortizedInvestment;
        return this;
    }

    @JsonProperty("annualCost")
    public Integer getAnnualCost() {
        return annualCost;
    }

    @JsonProperty("annualCost")
    public ScenarioAnalysis setAnnualCost(Integer annualCost) {
        this.annualCost = annualCost;
        return this;
    }

    @JsonProperty("annualEarnedMachineHours")
    public Integer getAnnualEarnedMachineHours() {
        return annualEarnedMachineHours;
    }

    @JsonProperty("annualEarnedMachineHours")
    public ScenarioAnalysis setAnnualEarnedMachineHours(Integer annualEarnedMachineHours) {
        this.annualEarnedMachineHours = annualEarnedMachineHours;
        return this;
    }

    @JsonProperty("annualMaintenanceFactor")
    public Integer getAnnualMaintenanceFactor() {
        return annualMaintenanceFactor;
    }

    @JsonProperty("annualMaintenanceFactor")
    public ScenarioAnalysis setAnnualMaintenanceFactor(Integer annualMaintenanceFactor) {
        this.annualMaintenanceFactor = annualMaintenanceFactor;
        return this;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public ScenarioAnalysis setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    @JsonProperty("batchCost")
    public Integer getBatchCost() {
        return batchCost;
    }

    @JsonProperty("batchCost")
    public ScenarioAnalysis setBatchCost(Integer batchCost) {
        this.batchCost = batchCost;
        return this;
    }

    @JsonProperty("batchSetupTime")
    public Integer getBatchSetupTime() {
        return batchSetupTime;
    }

    @JsonProperty("batchSetupTime")
    public ScenarioAnalysis setBatchSetupTime(Integer batchSetupTime) {
        this.batchSetupTime = batchSetupTime;
        return this;
    }

    @JsonProperty("batchSize")
    public Integer getBatchSize() {
        return batchSize;
    }

    @JsonProperty("batchSize")
    public ScenarioAnalysis setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @JsonProperty("cadSerLength")
    public Integer getCadSerLength() {
        return cadSerLength;
    }

    @JsonProperty("cadSerLength")
    public ScenarioAnalysis setCadSerLength(Integer cadSerLength) {
        this.cadSerLength = cadSerLength;
        return this;
    }

    @JsonProperty("cadSerWidth")
    public Integer getCadSerWidth() {
        return cadSerWidth;
    }

    @JsonProperty("cadSerWidth")
    public ScenarioAnalysis setCadSerWidth(Integer cadSerWidth) {
        this.cadSerWidth = cadSerWidth;
        return this;
    }

    @JsonProperty("capitalInvestment")
    public Integer getCapitalInvestment() {
        return capitalInvestment;
    }

    @JsonProperty("capitalInvestment")
    public ScenarioAnalysis setCapitalInvestment(Integer capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
        return this;
    }

    @JsonProperty("costingStatus")
    public String getCostingStatus() {
        return costingStatus;
    }

    @JsonProperty("costingStatus")
    public ScenarioAnalysis setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
        return this;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public ScenarioAnalysis setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public ScenarioAnalysis setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public ScenarioAnalysis setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    @JsonProperty("cycleTime")
    public Integer getCycleTime() {
        return cycleTime;
    }

    @JsonProperty("cycleTime")
    public ScenarioAnalysis setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public ScenarioAnalysis setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public ScenarioAnalysis setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public ScenarioAnalysis setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    @JsonProperty("depreciationCost")
    public Integer getDepreciationCost() {
        return depreciationCost;
    }

    @JsonProperty("depreciationCost")
    public ScenarioAnalysis setDepreciationCost(Integer depreciationCost) {
        this.depreciationCost = depreciationCost;
        return this;
    }

    @JsonProperty("dfmRisk")
    public Integer getDfmRisk() {
        return dfmRisk;
    }

    @JsonProperty("dfmRisk")
    public ScenarioAnalysis setDfmRisk(Integer dfmRisk) {
        this.dfmRisk = dfmRisk;
        return this;
    }

    @JsonProperty("directOverheadCost")
    public Integer getDirectOverheadCost() {
        return directOverheadCost;
    }

    @JsonProperty("directOverheadCost")
    public ScenarioAnalysis setDirectOverheadCost(Integer directOverheadCost) {
        this.directOverheadCost = directOverheadCost;
        return this;
    }

    @JsonProperty("directOverheadRate")
    public Integer getDirectOverheadRate() {
        return directOverheadRate;
    }

    @JsonProperty("directOverheadRate")
    public ScenarioAnalysis setDirectOverheadRate(Integer directOverheadRate) {
        this.directOverheadRate = directOverheadRate;
        return this;
    }

    @JsonProperty("dtcMessagesCount")
    public Integer getDtcMessagesCount() {
        return dtcMessagesCount;
    }

    @JsonProperty("dtcMessagesCount")
    public ScenarioAnalysis setDtcMessagesCount(Integer dtcMessagesCount) {
        this.dtcMessagesCount = dtcMessagesCount;
        return this;
    }

    @JsonProperty("elapsedTime")
    public Integer getElapsedTime() {
        return elapsedTime;
    }

    @JsonProperty("elapsedTime")
    public ScenarioAnalysis setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    @JsonProperty("electricityRate")
    public Integer getElectricityRate() {
        return electricityRate;
    }

    @JsonProperty("electricityRate")
    public ScenarioAnalysis setElectricityRate(Integer electricityRate) {
        this.electricityRate = electricityRate;
        return this;
    }

    @JsonProperty("energyCost")
    public Integer getEnergyCost() {
        return energyCost;
    }

    @JsonProperty("energyCost")
    public ScenarioAnalysis setEnergyCost(Integer energyCost) {
        this.energyCost = energyCost;
        return this;
    }

    @JsonProperty("engineeringDepartmentCost")
    public Integer getEngineeringDepartmentCost() {
        return engineeringDepartmentCost;
    }

    @JsonProperty("engineeringDepartmentCost")
    public ScenarioAnalysis setEngineeringDepartmentCost(Integer engineeringDepartmentCost) {
        this.engineeringDepartmentCost = engineeringDepartmentCost;
        return this;
    }

    @JsonProperty("engineeringSupportAllocation")
    public Integer getEngineeringSupportAllocation() {
        return engineeringSupportAllocation;
    }

    @JsonProperty("engineeringSupportAllocation")
    public ScenarioAnalysis setEngineeringSupportAllocation(Integer engineeringSupportAllocation) {
        this.engineeringSupportAllocation = engineeringSupportAllocation;
        return this;
    }

    @JsonProperty("engineeringSupportRate")
    public Integer getEngineeringSupportRate() {
        return engineeringSupportRate;
    }

    @JsonProperty("engineeringSupportRate")
    public ScenarioAnalysis setEngineeringSupportRate(Integer engineeringSupportRate) {
        this.engineeringSupportRate = engineeringSupportRate;
        return this;
    }

    @JsonProperty("expendableToolingCost")
    public Integer getExpendableToolingCost() {
        return expendableToolingCost;
    }

    @JsonProperty("expendableToolingCost")
    public ScenarioAnalysis setExpendableToolingCost(Integer expendableToolingCost) {
        this.expendableToolingCost = expendableToolingCost;
        return this;
    }

    @JsonProperty("expendableToolingCostPerCore")
    public Integer getExpendableToolingCostPerCore() {
        return expendableToolingCostPerCore;
    }

    @JsonProperty("expendableToolingCostPerCore")
    public ScenarioAnalysis setExpendableToolingCostPerCore(Integer expendableToolingCostPerCore) {
        this.expendableToolingCostPerCore = expendableToolingCostPerCore;
        return this;
    }

    @JsonProperty("expendableToolingCostPerPart")
    public Integer getExpendableToolingCostPerPart() {
        return expendableToolingCostPerPart;
    }

    @JsonProperty("expendableToolingCostPerPart")
    public ScenarioAnalysis setExpendableToolingCostPerPart(Integer expendableToolingCostPerPart) {
        this.expendableToolingCostPerPart = expendableToolingCostPerPart;
        return this;
    }

    @JsonProperty("extraCosts")
    public Integer getExtraCosts() {
        return extraCosts;
    }

    @JsonProperty("extraCosts")
    public ScenarioAnalysis setExtraCosts(Integer extraCosts) {
        this.extraCosts = extraCosts;
        return this;
    }

    @JsonProperty("facilityDirectOverheadCost")
    public Integer getFacilityDirectOverheadCost() {
        return facilityDirectOverheadCost;
    }

    @JsonProperty("facilityDirectOverheadCost")
    public ScenarioAnalysis setFacilityDirectOverheadCost(Integer facilityDirectOverheadCost) {
        this.facilityDirectOverheadCost = facilityDirectOverheadCost;
        return this;
    }

    @JsonProperty("facilityElectricityCost")
    public Integer getFacilityElectricityCost() {
        return facilityElectricityCost;
    }

    @JsonProperty("facilityElectricityCost")
    public ScenarioAnalysis setFacilityElectricityCost(Integer facilityElectricityCost) {
        this.facilityElectricityCost = facilityElectricityCost;
        return this;
    }

    @JsonProperty("facilityElectricityFactor")
    public Integer getFacilityElectricityFactor() {
        return facilityElectricityFactor;
    }

    @JsonProperty("facilityElectricityFactor")
    public ScenarioAnalysis setFacilityElectricityFactor(Integer facilityElectricityFactor) {
        this.facilityElectricityFactor = facilityElectricityFactor;
        return this;
    }

    @JsonProperty("facilityHeatAndGasCost")
    public Integer getFacilityHeatAndGasCost() {
        return facilityHeatAndGasCost;
    }

    @JsonProperty("facilityHeatAndGasCost")
    public ScenarioAnalysis setFacilityHeatAndGasCost(Integer facilityHeatAndGasCost) {
        this.facilityHeatAndGasCost = facilityHeatAndGasCost;
        return this;
    }

    @JsonProperty("facilityHeatAndGasFactor")
    public Integer getFacilityHeatAndGasFactor() {
        return facilityHeatAndGasFactor;
    }

    @JsonProperty("facilityHeatAndGasFactor")
    public ScenarioAnalysis setFacilityHeatAndGasFactor(Integer facilityHeatAndGasFactor) {
        this.facilityHeatAndGasFactor = facilityHeatAndGasFactor;
        return this;
    }

    @JsonProperty("facilityIndirectOverheadCost")
    public Integer getFacilityIndirectOverheadCost() {
        return facilityIndirectOverheadCost;
    }

    @JsonProperty("facilityIndirectOverheadCost")
    public ScenarioAnalysis setFacilityIndirectOverheadCost(Integer facilityIndirectOverheadCost) {
        this.facilityIndirectOverheadCost = facilityIndirectOverheadCost;
        return this;
    }

    @JsonProperty("facilityWaterCost")
    public Integer getFacilityWaterCost() {
        return facilityWaterCost;
    }

    @JsonProperty("facilityWaterCost")
    public ScenarioAnalysis setFacilityWaterCost(Integer facilityWaterCost) {
        this.facilityWaterCost = facilityWaterCost;
        return this;
    }

    @JsonProperty("facilityWaterFactor")
    public Integer getFacilityWaterFactor() {
        return facilityWaterFactor;
    }

    @JsonProperty("facilityWaterFactor")
    public ScenarioAnalysis setFacilityWaterFactor(Integer facilityWaterFactor) {
        this.facilityWaterFactor = facilityWaterFactor;
        return this;
    }

    @JsonProperty("failedGcdsCount")
    public Integer getFailedGcdsCount() {
        return failedGcdsCount;
    }

    @JsonProperty("failedGcdsCount")
    public ScenarioAnalysis setFailedGcdsCount(Integer failedGcdsCount) {
        this.failedGcdsCount = failedGcdsCount;
        return this;
    }

    @JsonProperty("failuresWarningsCount")
    public Integer getFailuresWarningsCount() {
        return failuresWarningsCount;
    }

    @JsonProperty("failuresWarningsCount")
    public ScenarioAnalysis setFailuresWarningsCount(Integer failuresWarningsCount) {
        this.failuresWarningsCount = failuresWarningsCount;
        return this;
    }

    @JsonProperty("finalYield")
    public Integer getFinalYield() {
        return finalYield;
    }

    @JsonProperty("finalYield")
    public ScenarioAnalysis setFinalYield(Integer finalYield) {
        this.finalYield = finalYield;
        return this;
    }

    @JsonProperty("finishMass")
    public Integer getFinishMass() {
        return finishMass;
    }

    @JsonProperty("finishMass")
    public ScenarioAnalysis setFinishMass(Integer finishMass) {
        this.finishMass = finishMass;
        return this;
    }

    @JsonProperty("fireInsuranceCost")
    public Integer getFireInsuranceCost() {
        return fireInsuranceCost;
    }

    @JsonProperty("fireInsuranceCost")
    public ScenarioAnalysis setFireInsuranceCost(Integer fireInsuranceCost) {
        this.fireInsuranceCost = fireInsuranceCost;
        return this;
    }

    @JsonProperty("fireInsuranceFactor")
    public Integer getFireInsuranceFactor() {
        return fireInsuranceFactor;
    }

    @JsonProperty("fireInsuranceFactor")
    public ScenarioAnalysis setFireInsuranceFactor(Integer fireInsuranceFactor) {
        this.fireInsuranceFactor = fireInsuranceFactor;
        return this;
    }

    @JsonProperty("fixtureCost")
    public Integer getFixtureCost() {
        return fixtureCost;
    }

    @JsonProperty("fixtureCost")
    public ScenarioAnalysis setFixtureCost(Integer fixtureCost) {
        this.fixtureCost = fixtureCost;
        return this;
    }

    @JsonProperty("fixtureCostPerPart")
    public Integer getFixtureCostPerPart() {
        return fixtureCostPerPart;
    }

    @JsonProperty("fixtureCostPerPart")
    public ScenarioAnalysis setFixtureCostPerPart(Integer fixtureCostPerPart) {
        this.fixtureCostPerPart = fixtureCostPerPart;
        return this;
    }

    @JsonProperty("footprintAllowanceFactor")
    public Integer getFootprintAllowanceFactor() {
        return footprintAllowanceFactor;
    }

    @JsonProperty("footprintAllowanceFactor")
    public ScenarioAnalysis setFootprintAllowanceFactor(Integer footprintAllowanceFactor) {
        this.footprintAllowanceFactor = footprintAllowanceFactor;
        return this;
    }

    @JsonProperty("fullyBurdenedCost")
    public Integer getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    @JsonProperty("fullyBurdenedCost")
    public ScenarioAnalysis setFullyBurdenedCost(Integer fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    @JsonProperty("gasRate")
    public Integer getGasRate() {
        return gasRate;
    }

    @JsonProperty("gasRate")
    public ScenarioAnalysis setGasRate(Integer gasRate) {
        this.gasRate = gasRate;
        return this;
    }

    @JsonProperty("gcdWithTolerancesCount")
    public Integer getGcdWithTolerancesCount() {
        return gcdWithTolerancesCount;
    }

    @JsonProperty("gcdWithTolerancesCount")
    public ScenarioAnalysis setGcdWithTolerancesCount(Integer gcdWithTolerancesCount) {
        this.gcdWithTolerancesCount = gcdWithTolerancesCount;
        return this;
    }

    @JsonProperty("goodPartYield")
    public Integer getGoodPartYield() {
        return goodPartYield;
    }

    @JsonProperty("goodPartYield")
    public ScenarioAnalysis setGoodPartYield(Integer goodPartYield) {
        this.goodPartYield = goodPartYield;
        return this;
    }

    @JsonProperty("hardToolingCost")
    public Integer getHardToolingCost() {
        return hardToolingCost;
    }

    @JsonProperty("hardToolingCost")
    public ScenarioAnalysis setHardToolingCost(Integer hardToolingCost) {
        this.hardToolingCost = hardToolingCost;
        return this;
    }

    @JsonProperty("hardToolingCostPerPart")
    public Integer getHardToolingCostPerPart() {
        return hardToolingCostPerPart;
    }

    @JsonProperty("hardToolingCostPerPart")
    public ScenarioAnalysis setHardToolingCostPerPart(Integer hardToolingCostPerPart) {
        this.hardToolingCostPerPart = hardToolingCostPerPart;
        return this;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public ScenarioAnalysis setHeight(Integer height) {
        this.height = height;
        return this;
    }

    @JsonProperty("hoursPerShift")
    public Integer getHoursPerShift() {
        return hoursPerShift;
    }

    @JsonProperty("hoursPerShift")
    public ScenarioAnalysis setHoursPerShift(Integer hoursPerShift) {
        this.hoursPerShift = hoursPerShift;
        return this;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public ScenarioAnalysis setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    @JsonProperty("imputedInterestCost")
    public Integer getImputedInterestCost() {
        return imputedInterestCost;
    }

    @JsonProperty("imputedInterestCost")
    public ScenarioAnalysis setImputedInterestCost(Integer imputedInterestCost) {
        this.imputedInterestCost = imputedInterestCost;
        return this;
    }

    @JsonProperty("imputedInterestRate")
    public Integer getImputedInterestRate() {
        return imputedInterestRate;
    }

    @JsonProperty("imputedInterestRate")
    public ScenarioAnalysis setImputedInterestRate(Integer imputedInterestRate) {
        this.imputedInterestRate = imputedInterestRate;
        return this;
    }

    @JsonProperty("indirectOverheadPercent")
    public Integer getIndirectOverheadPercent() {
        return indirectOverheadPercent;
    }

    @JsonProperty("indirectOverheadPercent")
    public ScenarioAnalysis setIndirectOverheadPercent(Integer indirectOverheadPercent) {
        this.indirectOverheadPercent = indirectOverheadPercent;
        return this;
    }

    @JsonProperty("indirectOverheadRate")
    public Integer getIndirectOverheadRate() {
        return indirectOverheadRate;
    }

    @JsonProperty("indirectOverheadRate")
    public ScenarioAnalysis setIndirectOverheadRate(Integer indirectOverheadRate) {
        this.indirectOverheadRate = indirectOverheadRate;
        return this;
    }

    @JsonProperty("installationCost")
    public Integer getInstallationCost() {
        return installationCost;
    }

    @JsonProperty("installationCost")
    public ScenarioAnalysis setInstallationCost(Integer installationCost) {
        this.installationCost = installationCost;
        return this;
    }

    @JsonProperty("installationFactor")
    public Integer getInstallationFactor() {
        return installationFactor;
    }

    @JsonProperty("installationFactor")
    public ScenarioAnalysis setInstallationFactor(Integer installationFactor) {
        this.installationFactor = installationFactor;
        return this;
    }

    @JsonProperty("insuranceCost")
    public Integer getInsuranceCost() {
        return insuranceCost;
    }

    @JsonProperty("insuranceCost")
    public ScenarioAnalysis setInsuranceCost(Integer insuranceCost) {
        this.insuranceCost = insuranceCost;
        return this;
    }

    @JsonProperty("laborCost")
    public Integer getLaborCost() {
        return laborCost;
    }

    @JsonProperty("laborCost")
    public ScenarioAnalysis setLaborCost(Integer laborCost) {
        this.laborCost = laborCost;
        return this;
    }

    @JsonProperty("laborRate")
    public Integer getLaborRate() {
        return laborRate;
    }

    @JsonProperty("laborRate")
    public ScenarioAnalysis setLaborRate(Integer laborRate) {
        this.laborRate = laborRate;
        return this;
    }

    @JsonProperty("laborTime")
    public Integer getLaborTime() {
        return laborTime;
    }

    @JsonProperty("laborTime")
    public ScenarioAnalysis setLaborTime(Integer laborTime) {
        this.laborTime = laborTime;
        return this;
    }

    @JsonProperty("lastCosted")
    public String getLastCosted() {
        return lastCosted;
    }

    @JsonProperty("lastCosted")
    public ScenarioAnalysis setLastCosted(String lastCosted) {
        this.lastCosted = lastCosted;
        return this;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public ScenarioAnalysis setLength(Integer length) {
        this.length = length;
        return this;
    }

    @JsonProperty("liabilityInsuranceCost")
    public Integer getLiabilityInsuranceCost() {
        return liabilityInsuranceCost;
    }

    @JsonProperty("liabilityInsuranceCost")
    public ScenarioAnalysis setLiabilityInsuranceCost(Integer liabilityInsuranceCost) {
        this.liabilityInsuranceCost = liabilityInsuranceCost;
        return this;
    }

    @JsonProperty("liabilityInsuranceFactor")
    public Integer getLiabilityInsuranceFactor() {
        return liabilityInsuranceFactor;
    }

    @JsonProperty("liabilityInsuranceFactor")
    public ScenarioAnalysis setLiabilityInsuranceFactor(Integer liabilityInsuranceFactor) {
        this.liabilityInsuranceFactor = liabilityInsuranceFactor;
        return this;
    }

    @JsonProperty("lifetimeCost")
    public Integer getLifetimeCost() {
        return lifetimeCost;
    }

    @JsonProperty("lifetimeCost")
    public ScenarioAnalysis setLifetimeCost(Integer lifetimeCost) {
        this.lifetimeCost = lifetimeCost;
        return this;
    }

    @JsonProperty("logisticsCost")
    public Integer getLogisticsCost() {
        return logisticsCost;
    }

    @JsonProperty("logisticsCost")
    public ScenarioAnalysis setLogisticsCost(Integer logisticsCost) {
        this.logisticsCost = logisticsCost;
        return this;
    }

    @JsonProperty("lossInsuranceCost")
    public Integer getLossInsuranceCost() {
        return lossInsuranceCost;
    }

    @JsonProperty("lossInsuranceCost")
    public ScenarioAnalysis setLossInsuranceCost(Integer lossInsuranceCost) {
        this.lossInsuranceCost = lossInsuranceCost;
        return this;
    }

    @JsonProperty("lossInsuranceFactor")
    public Integer getLossInsuranceFactor() {
        return lossInsuranceFactor;
    }

    @JsonProperty("lossInsuranceFactor")
    public ScenarioAnalysis setLossInsuranceFactor(Integer lossInsuranceFactor) {
        this.lossInsuranceFactor = lossInsuranceFactor;
        return this;
    }

    @JsonProperty("machineLaborRateAdjustmentFactor")
    public Integer getMachineLaborRateAdjustmentFactor() {
        return machineLaborRateAdjustmentFactor;
    }

    @JsonProperty("machineLaborRateAdjustmentFactor")
    public ScenarioAnalysis setMachineLaborRateAdjustmentFactor(Integer machineLaborRateAdjustmentFactor) {
        this.machineLaborRateAdjustmentFactor = machineLaborRateAdjustmentFactor;
        return this;
    }

    @JsonProperty("machineLength")
    public Integer getMachineLength() {
        return machineLength;
    }

    @JsonProperty("machineLength")
    public ScenarioAnalysis setMachineLength(Integer machineLength) {
        this.machineLength = machineLength;
        return this;
    }

    @JsonProperty("machineLife")
    public Integer getMachineLife() {
        return machineLife;
    }

    @JsonProperty("machineLife")
    public ScenarioAnalysis setMachineLife(Integer machineLife) {
        this.machineLife = machineLife;
        return this;
    }

    @JsonProperty("machinePower")
    public Integer getMachinePower() {
        return machinePower;
    }

    @JsonProperty("machinePower")
    public ScenarioAnalysis setMachinePower(Integer machinePower) {
        this.machinePower = machinePower;
        return this;
    }

    @JsonProperty("machinePrice")
    public Integer getMachinePrice() {
        return machinePrice;
    }

    @JsonProperty("machinePrice")
    public ScenarioAnalysis setMachinePrice(Integer machinePrice) {
        this.machinePrice = machinePrice;
        return this;
    }

    @JsonProperty("machineUptime")
    public Integer getMachineUptime() {
        return machineUptime;
    }

    @JsonProperty("machineUptime")
    public ScenarioAnalysis setMachineUptime(Integer machineUptime) {
        this.machineUptime = machineUptime;
        return this;
    }

    @JsonProperty("machineWidth")
    public Integer getMachineWidth() {
        return machineWidth;
    }

    @JsonProperty("machineWidth")
    public ScenarioAnalysis setMachineWidth(Integer machineWidth) {
        this.machineWidth = machineWidth;
        return this;
    }

    @JsonProperty("maintenanceCost")
    public Integer getMaintenanceCost() {
        return maintenanceCost;
    }

    @JsonProperty("maintenanceCost")
    public ScenarioAnalysis setMaintenanceCost(Integer maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
        return this;
    }

    @JsonProperty("maintenanceDepartmentCost")
    public Integer getMaintenanceDepartmentCost() {
        return maintenanceDepartmentCost;
    }

    @JsonProperty("maintenanceDepartmentCost")
    public ScenarioAnalysis setMaintenanceDepartmentCost(Integer maintenanceDepartmentCost) {
        this.maintenanceDepartmentCost = maintenanceDepartmentCost;
        return this;
    }

    @JsonProperty("maintenanceSupportAllocation")
    public Integer getMaintenanceSupportAllocation() {
        return maintenanceSupportAllocation;
    }

    @JsonProperty("maintenanceSupportAllocation")
    public ScenarioAnalysis setMaintenanceSupportAllocation(Integer maintenanceSupportAllocation) {
        this.maintenanceSupportAllocation = maintenanceSupportAllocation;
        return this;
    }

    @JsonProperty("maintenanceSupportRate")
    public Integer getMaintenanceSupportRate() {
        return maintenanceSupportRate;
    }

    @JsonProperty("maintenanceSupportRate")
    public ScenarioAnalysis setMaintenanceSupportRate(Integer maintenanceSupportRate) {
        this.maintenanceSupportRate = maintenanceSupportRate;
        return this;
    }

    @JsonProperty("margin")
    public Integer getMargin() {
        return margin;
    }

    @JsonProperty("margin")
    public ScenarioAnalysis setMargin(Integer margin) {
        this.margin = margin;
        return this;
    }

    @JsonProperty("marginPercent")
    public Integer getMarginPercent() {
        return marginPercent;
    }

    @JsonProperty("marginPercent")
    public ScenarioAnalysis setMarginPercent(Integer marginPercent) {
        this.marginPercent = marginPercent;
        return this;
    }

    @JsonProperty("materialCost")
    public Integer getMaterialCost() {
        return materialCost;
    }

    @JsonProperty("materialCost")
    public ScenarioAnalysis setMaterialCost(Integer materialCost) {
        this.materialCost = materialCost;
        return this;
    }

    @JsonProperty("materialName")
    public String getMaterialName() {
        return materialName;
    }

    @JsonProperty("materialName")
    public ScenarioAnalysis setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    @JsonProperty("materialOverheadCost")
    public Integer getMaterialOverheadCost() {
        return materialOverheadCost;
    }

    @JsonProperty("materialOverheadCost")
    public ScenarioAnalysis setMaterialOverheadCost(Integer materialOverheadCost) {
        this.materialOverheadCost = materialOverheadCost;
        return this;
    }

    @JsonProperty("materialOverheadPercent")
    public Integer getMaterialOverheadPercent() {
        return materialOverheadPercent;
    }

    @JsonProperty("materialOverheadPercent")
    public ScenarioAnalysis setMaterialOverheadPercent(Integer materialOverheadPercent) {
        this.materialOverheadPercent = materialOverheadPercent;
        return this;
    }

    @JsonProperty("materialStockFormName")
    public String getMaterialStockFormName() {
        return materialStockFormName;
    }

    @JsonProperty("materialStockFormName")
    public ScenarioAnalysis setMaterialStockFormName(String materialStockFormName) {
        this.materialStockFormName = materialStockFormName;
        return this;
    }

    @JsonProperty("materialStockName")
    public String getMaterialStockName() {
        return materialStockName;
    }

    @JsonProperty("materialStockName")
    public ScenarioAnalysis setMaterialStockName(String materialStockName) {
        this.materialStockName = materialStockName;
        return this;
    }

    @JsonProperty("materialUnitCost")
    public Integer getMaterialUnitCost() {
        return materialUnitCost;
    }

    @JsonProperty("materialUnitCost")
    public ScenarioAnalysis setMaterialUnitCost(Integer materialUnitCost) {
        this.materialUnitCost = materialUnitCost;
        return this;
    }

    @JsonProperty("materialYield")
    public Integer getMaterialYield() {
        return materialYield;
    }

    @JsonProperty("materialYield")
    public ScenarioAnalysis setMaterialYield(Integer materialYield) {
        this.materialYield = materialYield;
        return this;
    }

    @JsonProperty("nonProductionFootprintFactor")
    public Integer getNonProductionFootprintFactor() {
        return nonProductionFootprintFactor;
    }

    @JsonProperty("nonProductionFootprintFactor")
    public ScenarioAnalysis setNonProductionFootprintFactor(Integer nonProductionFootprintFactor) {
        this.nonProductionFootprintFactor = nonProductionFootprintFactor;
        return this;
    }

    @JsonProperty("notSupportedGcdsCount")
    public Integer getNotSupportedGcdsCount() {
        return notSupportedGcdsCount;
    }

    @JsonProperty("notSupportedGcdsCount")
    public ScenarioAnalysis setNotSupportedGcdsCount(Integer notSupportedGcdsCount) {
        this.notSupportedGcdsCount = notSupportedGcdsCount;
        return this;
    }

    @JsonProperty("numOperators")
    public Integer getNumOperators() {
        return numOperators;
    }

    @JsonProperty("numOperators")
    public ScenarioAnalysis setNumOperators(Integer numOperators) {
        this.numOperators = numOperators;
        return this;
    }

    @JsonProperty("numPartsPerSheet")
    public Integer getNumPartsPerSheet() {
        return numPartsPerSheet;
    }

    @JsonProperty("numPartsPerSheet")
    public ScenarioAnalysis setNumPartsPerSheet(Integer numPartsPerSheet) {
        this.numPartsPerSheet = numPartsPerSheet;
        return this;
    }

    @JsonProperty("numScrapParts")
    public Integer getNumScrapParts() {
        return numScrapParts;
    }

    @JsonProperty("numScrapParts")
    public ScenarioAnalysis setNumScrapParts(Integer numScrapParts) {
        this.numScrapParts = numScrapParts;
        return this;
    }

    @JsonProperty("numScrapPartsDownStream")
    public Integer getNumScrapPartsDownStream() {
        return numScrapPartsDownStream;
    }

    @JsonProperty("numScrapPartsDownStream")
    public ScenarioAnalysis setNumScrapPartsDownStream(Integer numScrapPartsDownStream) {
        this.numScrapPartsDownStream = numScrapPartsDownStream;
        return this;
    }

    @JsonProperty("numberOfParts")
    public Integer getNumberOfParts() {
        return numberOfParts;
    }

    @JsonProperty("numberOfParts")
    public ScenarioAnalysis setNumberOfParts(Integer numberOfParts) {
        this.numberOfParts = numberOfParts;
        return this;
    }

    @JsonProperty("otherDirectCosts")
    public Integer getOtherDirectCosts() {
        return otherDirectCosts;
    }

    @JsonProperty("otherDirectCosts")
    public ScenarioAnalysis setOtherDirectCosts(Integer otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
        return this;
    }

    @JsonProperty("overheadRate")
    public Integer getOverheadRate() {
        return overheadRate;
    }

    @JsonProperty("overheadRate")
    public ScenarioAnalysis setOverheadRate(Integer overheadRate) {
        this.overheadRate = overheadRate;
        return this;
    }

    @JsonProperty("partsPerHour")
    public Integer getPartsPerHour() {
        return partsPerHour;
    }

    @JsonProperty("partsPerHour")
    public ScenarioAnalysis setPartsPerHour(Integer partsPerHour) {
        this.partsPerHour = partsPerHour;
        return this;
    }

    @JsonProperty("periodOverhead")
    public Integer getPeriodOverhead() {
        return periodOverhead;
    }

    @JsonProperty("periodOverhead")
    public ScenarioAnalysis setPeriodOverhead(Integer periodOverhead) {
        this.periodOverhead = periodOverhead;
        return this;
    }

    @JsonProperty("pieceAndPeriod")
    public Integer getPieceAndPeriod() {
        return pieceAndPeriod;
    }

    @JsonProperty("pieceAndPeriod")
    public ScenarioAnalysis setPieceAndPeriod(Integer pieceAndPeriod) {
        this.pieceAndPeriod = pieceAndPeriod;
        return this;
    }

    @JsonProperty("pieceCost")
    public Integer getPieceCost() {
        return pieceCost;
    }

    @JsonProperty("pieceCost")
    public ScenarioAnalysis setPieceCost(Integer pieceCost) {
        this.pieceCost = pieceCost;
        return this;
    }

    @JsonProperty("plantLaborRateAdjustmentFactor")
    public Integer getPlantLaborRateAdjustmentFactor() {
        return plantLaborRateAdjustmentFactor;
    }

    @JsonProperty("plantLaborRateAdjustmentFactor")
    public ScenarioAnalysis setPlantLaborRateAdjustmentFactor(Integer plantLaborRateAdjustmentFactor) {
        this.plantLaborRateAdjustmentFactor = plantLaborRateAdjustmentFactor;
        return this;
    }

    @JsonProperty("processRoutingName")
    public String getProcessRoutingName() {
        return processRoutingName;
    }

    @JsonProperty("processRoutingName")
    public ScenarioAnalysis setProcessRoutingName(String processRoutingName) {
        this.processRoutingName = processRoutingName;
        return this;
    }

    @JsonProperty("productionDaysPerYear")
    public Integer getProductionDaysPerYear() {
        return productionDaysPerYear;
    }

    @JsonProperty("productionDaysPerYear")
    public ScenarioAnalysis setProductionDaysPerYear(Integer productionDaysPerYear) {
        this.productionDaysPerYear = productionDaysPerYear;
        return this;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public ScenarioAnalysis setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    @JsonProperty("programmingCost")
    public Integer getProgrammingCost() {
        return programmingCost;
    }

    @JsonProperty("programmingCost")
    public ScenarioAnalysis setProgrammingCost(Integer programmingCost) {
        this.programmingCost = programmingCost;
        return this;
    }

    @JsonProperty("programmingCostPerPart")
    public Integer getProgrammingCostPerPart() {
        return programmingCostPerPart;
    }

    @JsonProperty("programmingCostPerPart")
    public ScenarioAnalysis setProgrammingCostPerPart(Integer programmingCostPerPart) {
        this.programmingCostPerPart = programmingCostPerPart;
        return this;
    }

    @JsonProperty("purchasingDepartmentCost")
    public Integer getPurchasingDepartmentCost() {
        return purchasingDepartmentCost;
    }

    @JsonProperty("purchasingDepartmentCost")
    public ScenarioAnalysis setPurchasingDepartmentCost(Integer purchasingDepartmentCost) {
        this.purchasingDepartmentCost = purchasingDepartmentCost;
        return this;
    }

    @JsonProperty("purchasingPowerFactor")
    public Integer getPurchasingPowerFactor() {
        return purchasingPowerFactor;
    }

    @JsonProperty("purchasingPowerFactor")
    public ScenarioAnalysis setPurchasingPowerFactor(Integer purchasingPowerFactor) {
        this.purchasingPowerFactor = purchasingPowerFactor;
        return this;
    }

    @JsonProperty("purchasingSupportAllocation")
    public Integer getPurchasingSupportAllocation() {
        return purchasingSupportAllocation;
    }

    @JsonProperty("purchasingSupportAllocation")
    public ScenarioAnalysis setPurchasingSupportAllocation(Integer purchasingSupportAllocation) {
        this.purchasingSupportAllocation = purchasingSupportAllocation;
        return this;
    }

    @JsonProperty("purchasingSupportRate")
    public Integer getPurchasingSupportRate() {
        return purchasingSupportRate;
    }

    @JsonProperty("purchasingSupportRate")
    public ScenarioAnalysis setPurchasingSupportRate(Integer purchasingSupportRate) {
        this.purchasingSupportRate = purchasingSupportRate;
        return this;
    }

    @JsonProperty("qualityDepartmentCost")
    public Integer getQualityDepartmentCost() {
        return qualityDepartmentCost;
    }

    @JsonProperty("qualityDepartmentCost")
    public ScenarioAnalysis setQualityDepartmentCost(Integer qualityDepartmentCost) {
        this.qualityDepartmentCost = qualityDepartmentCost;
        return this;
    }

    @JsonProperty("qualitySupportAllocation")
    public Integer getQualitySupportAllocation() {
        return qualitySupportAllocation;
    }

    @JsonProperty("qualitySupportAllocation")
    public ScenarioAnalysis setQualitySupportAllocation(Integer qualitySupportAllocation) {
        this.qualitySupportAllocation = qualitySupportAllocation;
        return this;
    }

    @JsonProperty("qualitySupportRate")
    public Integer getQualitySupportRate() {
        return qualitySupportRate;
    }

    @JsonProperty("qualitySupportRate")
    public ScenarioAnalysis setQualitySupportRate(Integer qualitySupportRate) {
        this.qualitySupportRate = qualitySupportRate;
        return this;
    }

    @JsonProperty("rentCost")
    public Integer getRentCost() {
        return rentCost;
    }

    @JsonProperty("rentCost")
    public ScenarioAnalysis setRentCost(Integer rentCost) {
        this.rentCost = rentCost;
        return this;
    }

    @JsonProperty("rentRate")
    public Integer getRentRate() {
        return rentRate;
    }

    @JsonProperty("rentRate")
    public ScenarioAnalysis setRentRate(Integer rentRate) {
        this.rentRate = rentRate;
        return this;
    }

    @JsonProperty("roughLength")
    public Integer getRoughLength() {
        return roughLength;
    }

    @JsonProperty("roughLength")
    public ScenarioAnalysis setRoughLength(Integer roughLength) {
        this.roughLength = roughLength;
        return this;
    }

    @JsonProperty("roughMass")
    public Integer getRoughMass() {
        return roughMass;
    }

    @JsonProperty("roughMass")
    public ScenarioAnalysis setRoughMass(Integer roughMass) {
        this.roughMass = roughMass;
        return this;
    }

    @JsonProperty("salvageValue")
    public Integer getSalvageValue() {
        return salvageValue;
    }

    @JsonProperty("salvageValue")
    public ScenarioAnalysis setSalvageValue(Integer salvageValue) {
        this.salvageValue = salvageValue;
        return this;
    }

    @JsonProperty("salvageValueFactor")
    public Integer getSalvageValueFactor() {
        return salvageValueFactor;
    }

    @JsonProperty("salvageValueFactor")
    public ScenarioAnalysis setSalvageValueFactor(Integer salvageValueFactor) {
        this.salvageValueFactor = salvageValueFactor;
        return this;
    }

    @JsonProperty("scrapMass")
    public Integer getScrapMass() {
        return scrapMass;
    }

    @JsonProperty("scrapMass")
    public ScenarioAnalysis setScrapMass(Integer scrapMass) {
        this.scrapMass = scrapMass;
        return this;
    }

    @JsonProperty("scrapPartCredit")
    public Integer getScrapPartCredit() {
        return scrapPartCredit;
    }

    @JsonProperty("scrapPartCredit")
    public ScenarioAnalysis setScrapPartCredit(Integer scrapPartCredit) {
        this.scrapPartCredit = scrapPartCredit;
        return this;
    }

    @JsonProperty("setupCostPerCore")
    public Integer getSetupCostPerCore() {
        return setupCostPerCore;
    }

    @JsonProperty("setupCostPerCore")
    public ScenarioAnalysis setSetupCostPerCore(Integer setupCostPerCore) {
        this.setupCostPerCore = setupCostPerCore;
        return this;
    }

    @JsonProperty("setupCostPerPart")
    public Integer getSetupCostPerPart() {
        return setupCostPerPart;
    }

    @JsonProperty("setupCostPerPart")
    public ScenarioAnalysis setSetupCostPerPart(Integer setupCostPerPart) {
        this.setupCostPerPart = setupCostPerPart;
        return this;
    }

    @JsonProperty("sgaCost")
    public Integer getSgaCost() {
        return sgaCost;
    }

    @JsonProperty("sgaCost")
    public ScenarioAnalysis setSgaCost(Integer sgaCost) {
        this.sgaCost = sgaCost;
        return this;
    }

    @JsonProperty("sgaPercent")
    public Integer getSgaPercent() {
        return sgaPercent;
    }

    @JsonProperty("sgaPercent")
    public ScenarioAnalysis setSgaPercent(Integer sgaPercent) {
        this.sgaPercent = sgaPercent;
        return this;
    }

    @JsonProperty("shiftsPerDay")
    public Integer getShiftsPerDay() {
        return shiftsPerDay;
    }

    @JsonProperty("shiftsPerDay")
    public ScenarioAnalysis setShiftsPerDay(Integer shiftsPerDay) {
        this.shiftsPerDay = shiftsPerDay;
        return this;
    }

    @JsonProperty("skillLevel")
    public Integer getSkillLevel() {
        return skillLevel;
    }

    @JsonProperty("skillLevel")
    public ScenarioAnalysis setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
        return this;
    }

    @JsonProperty("stockPropertyHeight")
    public Integer getStockPropertyHeight() {
        return stockPropertyHeight;
    }

    @JsonProperty("stockPropertyHeight")
    public ScenarioAnalysis setStockPropertyHeight(Integer stockPropertyHeight) {
        this.stockPropertyHeight = stockPropertyHeight;
        return this;
    }

    @JsonProperty("stockPropertyInsideDia")
    public Integer getStockPropertyInsideDia() {
        return stockPropertyInsideDia;
    }

    @JsonProperty("stockPropertyInsideDia")
    public ScenarioAnalysis setStockPropertyInsideDia(Integer stockPropertyInsideDia) {
        this.stockPropertyInsideDia = stockPropertyInsideDia;
        return this;
    }

    @JsonProperty("stockPropertyLength")
    public Integer getStockPropertyLength() {
        return stockPropertyLength;
    }

    @JsonProperty("stockPropertyLength")
    public ScenarioAnalysis setStockPropertyLength(Integer stockPropertyLength) {
        this.stockPropertyLength = stockPropertyLength;
        return this;
    }

    @JsonProperty("stockPropertyOutsideDia")
    public Integer getStockPropertyOutsideDia() {
        return stockPropertyOutsideDia;
    }

    @JsonProperty("stockPropertyOutsideDia")
    public ScenarioAnalysis setStockPropertyOutsideDia(Integer stockPropertyOutsideDia) {
        this.stockPropertyOutsideDia = stockPropertyOutsideDia;
        return this;
    }

    @JsonProperty("stockPropertyThickness")
    public Integer getStockPropertyThickness() {
        return stockPropertyThickness;
    }

    @JsonProperty("stockPropertyThickness")
    public ScenarioAnalysis setStockPropertyThickness(Integer stockPropertyThickness) {
        this.stockPropertyThickness = stockPropertyThickness;
        return this;
    }

    @JsonProperty("stockPropertyWallThickness")
    public Integer getStockPropertyWallThickness() {
        return stockPropertyWallThickness;
    }

    @JsonProperty("stockPropertyWallThickness")
    public ScenarioAnalysis setStockPropertyWallThickness(Integer stockPropertyWallThickness) {
        this.stockPropertyWallThickness = stockPropertyWallThickness;
        return this;
    }

    @JsonProperty("stockPropertyWidth")
    public Integer getStockPropertyWidth() {
        return stockPropertyWidth;
    }

    @JsonProperty("stockPropertyWidth")
    public ScenarioAnalysis setStockPropertyWidth(Integer stockPropertyWidth) {
        this.stockPropertyWidth = stockPropertyWidth;
        return this;
    }

    @JsonProperty("stripNestingPitch")
    public Integer getStripNestingPitch() {
        return stripNestingPitch;
    }

    @JsonProperty("stripNestingPitch")
    public ScenarioAnalysis setStripNestingPitch(Integer stripNestingPitch) {
        this.stripNestingPitch = stripNestingPitch;
        return this;
    }

    @JsonProperty("suppliesCost")
    public Integer getSuppliesCost() {
        return suppliesCost;
    }

    @JsonProperty("suppliesCost")
    public ScenarioAnalysis setSuppliesCost(Integer suppliesCost) {
        this.suppliesCost = suppliesCost;
        return this;
    }

    @JsonProperty("supportAllocation")
    public Integer getSupportAllocation() {
        return supportAllocation;
    }

    @JsonProperty("supportAllocation")
    public ScenarioAnalysis setSupportAllocation(Integer supportAllocation) {
        this.supportAllocation = supportAllocation;
        return this;
    }

    @JsonProperty("supportServicesCost")
    public Integer getSupportServicesCost() {
        return supportServicesCost;
    }

    @JsonProperty("supportServicesCost")
    public ScenarioAnalysis setSupportServicesCost(Integer supportServicesCost) {
        this.supportServicesCost = supportServicesCost;
        return this;
    }

    @JsonProperty("toolCribDepartmentCost")
    public Integer getToolCribDepartmentCost() {
        return toolCribDepartmentCost;
    }

    @JsonProperty("toolCribDepartmentCost")
    public ScenarioAnalysis setToolCribDepartmentCost(Integer toolCribDepartmentCost) {
        this.toolCribDepartmentCost = toolCribDepartmentCost;
        return this;
    }

    @JsonProperty("toolCribSupportAllocation")
    public Integer getToolCribSupportAllocation() {
        return toolCribSupportAllocation;
    }

    @JsonProperty("toolCribSupportAllocation")
    public ScenarioAnalysis setToolCribSupportAllocation(Integer toolCribSupportAllocation) {
        this.toolCribSupportAllocation = toolCribSupportAllocation;
        return this;
    }

    @JsonProperty("toolCribSupportRate")
    public Integer getToolCribSupportRate() {
        return toolCribSupportRate;
    }

    @JsonProperty("toolCribSupportRate")
    public ScenarioAnalysis setToolCribSupportRate(Integer toolCribSupportRate) {
        this.toolCribSupportRate = toolCribSupportRate;
        return this;
    }

    @JsonProperty("toolingCostPerPart")
    public Integer getToolingCostPerPart() {
        return toolingCostPerPart;
    }

    @JsonProperty("toolingCostPerPart")
    public ScenarioAnalysis setToolingCostPerPart(Integer toolingCostPerPart) {
        this.toolingCostPerPart = toolingCostPerPart;
        return this;
    }

    @JsonProperty("totalCost")
    public Integer getTotalCost() {
        return totalCost;
    }

    @JsonProperty("totalCost")
    public ScenarioAnalysis setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    @JsonProperty("totalMachineCost")
    public Integer getTotalMachineCost() {
        return totalMachineCost;
    }

    @JsonProperty("totalMachineCost")
    public ScenarioAnalysis setTotalMachineCost(Integer totalMachineCost) {
        this.totalMachineCost = totalMachineCost;
        return this;
    }

    @JsonProperty("totalProductionVolume")
    public Integer getTotalProductionVolume() {
        return totalProductionVolume;
    }

    @JsonProperty("totalProductionVolume")
    public ScenarioAnalysis setTotalProductionVolume(Integer totalProductionVolume) {
        this.totalProductionVolume = totalProductionVolume;
        return this;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public ScenarioAnalysis setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public ScenarioAnalysis setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public ScenarioAnalysis setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    @JsonProperty("useComputedOverheadRate")
    public Boolean getUseComputedOverheadRate() {
        return useComputedOverheadRate;
    }

    @JsonProperty("useComputedOverheadRate")
    public ScenarioAnalysis setUseComputedOverheadRate(Boolean useComputedOverheadRate) {
        this.useComputedOverheadRate = useComputedOverheadRate;
        return this;
    }

    @JsonProperty("useIndirectOverheadPercentage")
    public Boolean getUseIndirectOverheadPercentage() {
        return useIndirectOverheadPercentage;
    }

    @JsonProperty("useIndirectOverheadPercentage")
    public ScenarioAnalysis setUseIndirectOverheadPercentage(Boolean useIndirectOverheadPercentage) {
        this.useIndirectOverheadPercentage = useIndirectOverheadPercentage;
        return this;
    }

    @JsonProperty("utilitiesCost")
    public Integer getUtilitiesCost() {
        return utilitiesCost;
    }

    @JsonProperty("utilitiesCost")
    public ScenarioAnalysis setUtilitiesCost(Integer utilitiesCost) {
        this.utilitiesCost = utilitiesCost;
        return this;
    }

    @JsonProperty("utilization")
    public Integer getUtilization() {
        return utilization;
    }

    @JsonProperty("utilization")
    public ScenarioAnalysis setUtilization(Integer utilization) {
        this.utilization = utilization;
        return this;
    }

    @JsonProperty("utilizationWithAddendum")
    public Integer getUtilizationWithAddendum() {
        return utilizationWithAddendum;
    }

    @JsonProperty("utilizationWithAddendum")
    public ScenarioAnalysis setUtilizationWithAddendum(Integer utilizationWithAddendum) {
        this.utilizationWithAddendum = utilizationWithAddendum;
        return this;
    }

    @JsonProperty("utilizationWithoutAddendum")
    public Integer getUtilizationWithoutAddendum() {
        return utilizationWithoutAddendum;
    }

    @JsonProperty("utilizationWithoutAddendum")
    public ScenarioAnalysis setUtilizationWithoutAddendum(Integer utilizationWithoutAddendum) {
        this.utilizationWithoutAddendum = utilizationWithoutAddendum;
        return this;
    }

    @JsonProperty("virtualMaterialStockName")
    public String getVirtualMaterialStockName() {
        return virtualMaterialStockName;
    }

    @JsonProperty("virtualMaterialStockName")
    public ScenarioAnalysis setVirtualMaterialStockName(String virtualMaterialStockName) {
        this.virtualMaterialStockName = virtualMaterialStockName;
        return this;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public ScenarioAnalysis setWidth(Integer width) {
        this.width = width;
        return this;
    }

    @JsonProperty("workCenterCapacity")
    public Integer getWorkCenterCapacity() {
        return workCenterCapacity;
    }

    @JsonProperty("workCenterCapacity")
    public ScenarioAnalysis setWorkCenterCapacity(Integer workCenterCapacity) {
        this.workCenterCapacity = workCenterCapacity;
        return this;
    }

    @JsonProperty("workCenterFootprint")
    public Integer getWorkCenterFootprint() {
        return workCenterFootprint;
    }

    @JsonProperty("workCenterFootprint")
    public ScenarioAnalysis setWorkCenterFootprint(Integer workCenterFootprint) {
        this.workCenterFootprint = workCenterFootprint;
        return this;
    }
}