package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("addFacilityCostToDirOverhead")
    public Boolean getAddFacilityCostToDirOverhead() {
        return addFacilityCostToDirOverhead;
    }

    @JsonProperty("addFacilityCostToDirOverhead")
    public void setAddFacilityCostToDirOverhead(Boolean addFacilityCostToDirOverhead) {
        this.addFacilityCostToDirOverhead = addFacilityCostToDirOverhead;
    }

    @JsonProperty("additionalAmortizedInvestment")
    public Integer getAdditionalAmortizedInvestment() {
        return additionalAmortizedInvestment;
    }

    @JsonProperty("additionalAmortizedInvestment")
    public void setAdditionalAmortizedInvestment(Integer additionalAmortizedInvestment) {
        this.additionalAmortizedInvestment = additionalAmortizedInvestment;
    }

    @JsonProperty("additionalDirectCosts")
    public Integer getAdditionalDirectCosts() {
        return additionalDirectCosts;
    }

    @JsonProperty("additionalDirectCosts")
    public void setAdditionalDirectCosts(Integer additionalDirectCosts) {
        this.additionalDirectCosts = additionalDirectCosts;
    }

    @JsonProperty("amortizedInvestment")
    public Integer getAmortizedInvestment() {
        return amortizedInvestment;
    }

    @JsonProperty("amortizedInvestment")
    public void setAmortizedInvestment(Integer amortizedInvestment) {
        this.amortizedInvestment = amortizedInvestment;
    }

    @JsonProperty("annualCost")
    public Integer getAnnualCost() {
        return annualCost;
    }

    @JsonProperty("annualCost")
    public void setAnnualCost(Integer annualCost) {
        this.annualCost = annualCost;
    }

    @JsonProperty("annualEarnedMachineHours")
    public Integer getAnnualEarnedMachineHours() {
        return annualEarnedMachineHours;
    }

    @JsonProperty("annualEarnedMachineHours")
    public void setAnnualEarnedMachineHours(Integer annualEarnedMachineHours) {
        this.annualEarnedMachineHours = annualEarnedMachineHours;
    }

    @JsonProperty("annualMaintenanceFactor")
    public Integer getAnnualMaintenanceFactor() {
        return annualMaintenanceFactor;
    }

    @JsonProperty("annualMaintenanceFactor")
    public void setAnnualMaintenanceFactor(Integer annualMaintenanceFactor) {
        this.annualMaintenanceFactor = annualMaintenanceFactor;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public void setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
    }

    @JsonProperty("batchCost")
    public Integer getBatchCost() {
        return batchCost;
    }

    @JsonProperty("batchCost")
    public void setBatchCost(Integer batchCost) {
        this.batchCost = batchCost;
    }

    @JsonProperty("batchSetupTime")
    public Integer getBatchSetupTime() {
        return batchSetupTime;
    }

    @JsonProperty("batchSetupTime")
    public void setBatchSetupTime(Integer batchSetupTime) {
        this.batchSetupTime = batchSetupTime;
    }

    @JsonProperty("batchSize")
    public Integer getBatchSize() {
        return batchSize;
    }

    @JsonProperty("batchSize")
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @JsonProperty("cadSerLength")
    public Integer getCadSerLength() {
        return cadSerLength;
    }

    @JsonProperty("cadSerLength")
    public void setCadSerLength(Integer cadSerLength) {
        this.cadSerLength = cadSerLength;
    }

    @JsonProperty("cadSerWidth")
    public Integer getCadSerWidth() {
        return cadSerWidth;
    }

    @JsonProperty("cadSerWidth")
    public void setCadSerWidth(Integer cadSerWidth) {
        this.cadSerWidth = cadSerWidth;
    }

    @JsonProperty("capitalInvestment")
    public Integer getCapitalInvestment() {
        return capitalInvestment;
    }

    @JsonProperty("capitalInvestment")
    public void setCapitalInvestment(Integer capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
    }

    @JsonProperty("costingStatus")
    public String getCostingStatus() {
        return costingStatus;
    }

    @JsonProperty("costingStatus")
    public void setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @JsonProperty("cycleTime")
    public Integer getCycleTime() {
        return cycleTime;
    }

    @JsonProperty("cycleTime")
    public void setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public void setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
    }

    @JsonProperty("depreciationCost")
    public Integer getDepreciationCost() {
        return depreciationCost;
    }

    @JsonProperty("depreciationCost")
    public void setDepreciationCost(Integer depreciationCost) {
        this.depreciationCost = depreciationCost;
    }

    @JsonProperty("dfmRisk")
    public Integer getDfmRisk() {
        return dfmRisk;
    }

    @JsonProperty("dfmRisk")
    public void setDfmRisk(Integer dfmRisk) {
        this.dfmRisk = dfmRisk;
    }

    @JsonProperty("directOverheadCost")
    public Integer getDirectOverheadCost() {
        return directOverheadCost;
    }

    @JsonProperty("directOverheadCost")
    public void setDirectOverheadCost(Integer directOverheadCost) {
        this.directOverheadCost = directOverheadCost;
    }

    @JsonProperty("directOverheadRate")
    public Integer getDirectOverheadRate() {
        return directOverheadRate;
    }

    @JsonProperty("directOverheadRate")
    public void setDirectOverheadRate(Integer directOverheadRate) {
        this.directOverheadRate = directOverheadRate;
    }

    @JsonProperty("dtcMessagesCount")
    public Integer getDtcMessagesCount() {
        return dtcMessagesCount;
    }

    @JsonProperty("dtcMessagesCount")
    public void setDtcMessagesCount(Integer dtcMessagesCount) {
        this.dtcMessagesCount = dtcMessagesCount;
    }

    @JsonProperty("elapsedTime")
    public Integer getElapsedTime() {
        return elapsedTime;
    }

    @JsonProperty("elapsedTime")
    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @JsonProperty("electricityRate")
    public Integer getElectricityRate() {
        return electricityRate;
    }

    @JsonProperty("electricityRate")
    public void setElectricityRate(Integer electricityRate) {
        this.electricityRate = electricityRate;
    }

    @JsonProperty("energyCost")
    public Integer getEnergyCost() {
        return energyCost;
    }

    @JsonProperty("energyCost")
    public void setEnergyCost(Integer energyCost) {
        this.energyCost = energyCost;
    }

    @JsonProperty("engineeringDepartmentCost")
    public Integer getEngineeringDepartmentCost() {
        return engineeringDepartmentCost;
    }

    @JsonProperty("engineeringDepartmentCost")
    public void setEngineeringDepartmentCost(Integer engineeringDepartmentCost) {
        this.engineeringDepartmentCost = engineeringDepartmentCost;
    }

    @JsonProperty("engineeringSupportAllocation")
    public Integer getEngineeringSupportAllocation() {
        return engineeringSupportAllocation;
    }

    @JsonProperty("engineeringSupportAllocation")
    public void setEngineeringSupportAllocation(Integer engineeringSupportAllocation) {
        this.engineeringSupportAllocation = engineeringSupportAllocation;
    }

    @JsonProperty("engineeringSupportRate")
    public Integer getEngineeringSupportRate() {
        return engineeringSupportRate;
    }

    @JsonProperty("engineeringSupportRate")
    public void setEngineeringSupportRate(Integer engineeringSupportRate) {
        this.engineeringSupportRate = engineeringSupportRate;
    }

    @JsonProperty("expendableToolingCost")
    public Integer getExpendableToolingCost() {
        return expendableToolingCost;
    }

    @JsonProperty("expendableToolingCost")
    public void setExpendableToolingCost(Integer expendableToolingCost) {
        this.expendableToolingCost = expendableToolingCost;
    }

    @JsonProperty("expendableToolingCostPerCore")
    public Integer getExpendableToolingCostPerCore() {
        return expendableToolingCostPerCore;
    }

    @JsonProperty("expendableToolingCostPerCore")
    public void setExpendableToolingCostPerCore(Integer expendableToolingCostPerCore) {
        this.expendableToolingCostPerCore = expendableToolingCostPerCore;
    }

    @JsonProperty("expendableToolingCostPerPart")
    public Integer getExpendableToolingCostPerPart() {
        return expendableToolingCostPerPart;
    }

    @JsonProperty("expendableToolingCostPerPart")
    public void setExpendableToolingCostPerPart(Integer expendableToolingCostPerPart) {
        this.expendableToolingCostPerPart = expendableToolingCostPerPart;
    }

    @JsonProperty("extraCosts")
    public Integer getExtraCosts() {
        return extraCosts;
    }

    @JsonProperty("extraCosts")
    public void setExtraCosts(Integer extraCosts) {
        this.extraCosts = extraCosts;
    }

    @JsonProperty("facilityDirectOverheadCost")
    public Integer getFacilityDirectOverheadCost() {
        return facilityDirectOverheadCost;
    }

    @JsonProperty("facilityDirectOverheadCost")
    public void setFacilityDirectOverheadCost(Integer facilityDirectOverheadCost) {
        this.facilityDirectOverheadCost = facilityDirectOverheadCost;
    }

    @JsonProperty("facilityElectricityCost")
    public Integer getFacilityElectricityCost() {
        return facilityElectricityCost;
    }

    @JsonProperty("facilityElectricityCost")
    public void setFacilityElectricityCost(Integer facilityElectricityCost) {
        this.facilityElectricityCost = facilityElectricityCost;
    }

    @JsonProperty("facilityElectricityFactor")
    public Integer getFacilityElectricityFactor() {
        return facilityElectricityFactor;
    }

    @JsonProperty("facilityElectricityFactor")
    public void setFacilityElectricityFactor(Integer facilityElectricityFactor) {
        this.facilityElectricityFactor = facilityElectricityFactor;
    }

    @JsonProperty("facilityHeatAndGasCost")
    public Integer getFacilityHeatAndGasCost() {
        return facilityHeatAndGasCost;
    }

    @JsonProperty("facilityHeatAndGasCost")
    public void setFacilityHeatAndGasCost(Integer facilityHeatAndGasCost) {
        this.facilityHeatAndGasCost = facilityHeatAndGasCost;
    }

    @JsonProperty("facilityHeatAndGasFactor")
    public Integer getFacilityHeatAndGasFactor() {
        return facilityHeatAndGasFactor;
    }

    @JsonProperty("facilityHeatAndGasFactor")
    public void setFacilityHeatAndGasFactor(Integer facilityHeatAndGasFactor) {
        this.facilityHeatAndGasFactor = facilityHeatAndGasFactor;
    }

    @JsonProperty("facilityIndirectOverheadCost")
    public Integer getFacilityIndirectOverheadCost() {
        return facilityIndirectOverheadCost;
    }

    @JsonProperty("facilityIndirectOverheadCost")
    public void setFacilityIndirectOverheadCost(Integer facilityIndirectOverheadCost) {
        this.facilityIndirectOverheadCost = facilityIndirectOverheadCost;
    }

    @JsonProperty("facilityWaterCost")
    public Integer getFacilityWaterCost() {
        return facilityWaterCost;
    }

    @JsonProperty("facilityWaterCost")
    public void setFacilityWaterCost(Integer facilityWaterCost) {
        this.facilityWaterCost = facilityWaterCost;
    }

    @JsonProperty("facilityWaterFactor")
    public Integer getFacilityWaterFactor() {
        return facilityWaterFactor;
    }

    @JsonProperty("facilityWaterFactor")
    public void setFacilityWaterFactor(Integer facilityWaterFactor) {
        this.facilityWaterFactor = facilityWaterFactor;
    }

    @JsonProperty("failedGcdsCount")
    public Integer getFailedGcdsCount() {
        return failedGcdsCount;
    }

    @JsonProperty("failedGcdsCount")
    public void setFailedGcdsCount(Integer failedGcdsCount) {
        this.failedGcdsCount = failedGcdsCount;
    }

    @JsonProperty("failuresWarningsCount")
    public Integer getFailuresWarningsCount() {
        return failuresWarningsCount;
    }

    @JsonProperty("failuresWarningsCount")
    public void setFailuresWarningsCount(Integer failuresWarningsCount) {
        this.failuresWarningsCount = failuresWarningsCount;
    }

    @JsonProperty("finalYield")
    public Integer getFinalYield() {
        return finalYield;
    }

    @JsonProperty("finalYield")
    public void setFinalYield(Integer finalYield) {
        this.finalYield = finalYield;
    }

    @JsonProperty("finishMass")
    public Integer getFinishMass() {
        return finishMass;
    }

    @JsonProperty("finishMass")
    public void setFinishMass(Integer finishMass) {
        this.finishMass = finishMass;
    }

    @JsonProperty("fireInsuranceCost")
    public Integer getFireInsuranceCost() {
        return fireInsuranceCost;
    }

    @JsonProperty("fireInsuranceCost")
    public void setFireInsuranceCost(Integer fireInsuranceCost) {
        this.fireInsuranceCost = fireInsuranceCost;
    }

    @JsonProperty("fireInsuranceFactor")
    public Integer getFireInsuranceFactor() {
        return fireInsuranceFactor;
    }

    @JsonProperty("fireInsuranceFactor")
    public void setFireInsuranceFactor(Integer fireInsuranceFactor) {
        this.fireInsuranceFactor = fireInsuranceFactor;
    }

    @JsonProperty("fixtureCost")
    public Integer getFixtureCost() {
        return fixtureCost;
    }

    @JsonProperty("fixtureCost")
    public void setFixtureCost(Integer fixtureCost) {
        this.fixtureCost = fixtureCost;
    }

    @JsonProperty("fixtureCostPerPart")
    public Integer getFixtureCostPerPart() {
        return fixtureCostPerPart;
    }

    @JsonProperty("fixtureCostPerPart")
    public void setFixtureCostPerPart(Integer fixtureCostPerPart) {
        this.fixtureCostPerPart = fixtureCostPerPart;
    }

    @JsonProperty("footprintAllowanceFactor")
    public Integer getFootprintAllowanceFactor() {
        return footprintAllowanceFactor;
    }

    @JsonProperty("footprintAllowanceFactor")
    public void setFootprintAllowanceFactor(Integer footprintAllowanceFactor) {
        this.footprintAllowanceFactor = footprintAllowanceFactor;
    }

    @JsonProperty("fullyBurdenedCost")
    public Integer getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    @JsonProperty("fullyBurdenedCost")
    public void setFullyBurdenedCost(Integer fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
    }

    @JsonProperty("gasRate")
    public Integer getGasRate() {
        return gasRate;
    }

    @JsonProperty("gasRate")
    public void setGasRate(Integer gasRate) {
        this.gasRate = gasRate;
    }

    @JsonProperty("gcdWithTolerancesCount")
    public Integer getGcdWithTolerancesCount() {
        return gcdWithTolerancesCount;
    }

    @JsonProperty("gcdWithTolerancesCount")
    public void setGcdWithTolerancesCount(Integer gcdWithTolerancesCount) {
        this.gcdWithTolerancesCount = gcdWithTolerancesCount;
    }

    @JsonProperty("goodPartYield")
    public Integer getGoodPartYield() {
        return goodPartYield;
    }

    @JsonProperty("goodPartYield")
    public void setGoodPartYield(Integer goodPartYield) {
        this.goodPartYield = goodPartYield;
    }

    @JsonProperty("hardToolingCost")
    public Integer getHardToolingCost() {
        return hardToolingCost;
    }

    @JsonProperty("hardToolingCost")
    public void setHardToolingCost(Integer hardToolingCost) {
        this.hardToolingCost = hardToolingCost;
    }

    @JsonProperty("hardToolingCostPerPart")
    public Integer getHardToolingCostPerPart() {
        return hardToolingCostPerPart;
    }

    @JsonProperty("hardToolingCostPerPart")
    public void setHardToolingCostPerPart(Integer hardToolingCostPerPart) {
        this.hardToolingCostPerPart = hardToolingCostPerPart;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    @JsonProperty("hoursPerShift")
    public Integer getHoursPerShift() {
        return hoursPerShift;
    }

    @JsonProperty("hoursPerShift")
    public void setHoursPerShift(Integer hoursPerShift) {
        this.hoursPerShift = hoursPerShift;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JsonProperty("imputedInterestCost")
    public Integer getImputedInterestCost() {
        return imputedInterestCost;
    }

    @JsonProperty("imputedInterestCost")
    public void setImputedInterestCost(Integer imputedInterestCost) {
        this.imputedInterestCost = imputedInterestCost;
    }

    @JsonProperty("imputedInterestRate")
    public Integer getImputedInterestRate() {
        return imputedInterestRate;
    }

    @JsonProperty("imputedInterestRate")
    public void setImputedInterestRate(Integer imputedInterestRate) {
        this.imputedInterestRate = imputedInterestRate;
    }

    @JsonProperty("indirectOverheadPercent")
    public Integer getIndirectOverheadPercent() {
        return indirectOverheadPercent;
    }

    @JsonProperty("indirectOverheadPercent")
    public void setIndirectOverheadPercent(Integer indirectOverheadPercent) {
        this.indirectOverheadPercent = indirectOverheadPercent;
    }

    @JsonProperty("indirectOverheadRate")
    public Integer getIndirectOverheadRate() {
        return indirectOverheadRate;
    }

    @JsonProperty("indirectOverheadRate")
    public void setIndirectOverheadRate(Integer indirectOverheadRate) {
        this.indirectOverheadRate = indirectOverheadRate;
    }

    @JsonProperty("installationCost")
    public Integer getInstallationCost() {
        return installationCost;
    }

    @JsonProperty("installationCost")
    public void setInstallationCost(Integer installationCost) {
        this.installationCost = installationCost;
    }

    @JsonProperty("installationFactor")
    public Integer getInstallationFactor() {
        return installationFactor;
    }

    @JsonProperty("installationFactor")
    public void setInstallationFactor(Integer installationFactor) {
        this.installationFactor = installationFactor;
    }

    @JsonProperty("insuranceCost")
    public Integer getInsuranceCost() {
        return insuranceCost;
    }

    @JsonProperty("insuranceCost")
    public void setInsuranceCost(Integer insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    @JsonProperty("laborCost")
    public Integer getLaborCost() {
        return laborCost;
    }

    @JsonProperty("laborCost")
    public void setLaborCost(Integer laborCost) {
        this.laborCost = laborCost;
    }

    @JsonProperty("laborRate")
    public Integer getLaborRate() {
        return laborRate;
    }

    @JsonProperty("laborRate")
    public void setLaborRate(Integer laborRate) {
        this.laborRate = laborRate;
    }

    @JsonProperty("laborTime")
    public Integer getLaborTime() {
        return laborTime;
    }

    @JsonProperty("laborTime")
    public void setLaborTime(Integer laborTime) {
        this.laborTime = laborTime;
    }

    @JsonProperty("lastCosted")
    public String getLastCosted() {
        return lastCosted;
    }

    @JsonProperty("lastCosted")
    public void setLastCosted(String lastCosted) {
        this.lastCosted = lastCosted;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("liabilityInsuranceCost")
    public Integer getLiabilityInsuranceCost() {
        return liabilityInsuranceCost;
    }

    @JsonProperty("liabilityInsuranceCost")
    public void setLiabilityInsuranceCost(Integer liabilityInsuranceCost) {
        this.liabilityInsuranceCost = liabilityInsuranceCost;
    }

    @JsonProperty("liabilityInsuranceFactor")
    public Integer getLiabilityInsuranceFactor() {
        return liabilityInsuranceFactor;
    }

    @JsonProperty("liabilityInsuranceFactor")
    public void setLiabilityInsuranceFactor(Integer liabilityInsuranceFactor) {
        this.liabilityInsuranceFactor = liabilityInsuranceFactor;
    }

    @JsonProperty("lifetimeCost")
    public Integer getLifetimeCost() {
        return lifetimeCost;
    }

    @JsonProperty("lifetimeCost")
    public void setLifetimeCost(Integer lifetimeCost) {
        this.lifetimeCost = lifetimeCost;
    }

    @JsonProperty("logisticsCost")
    public Integer getLogisticsCost() {
        return logisticsCost;
    }

    @JsonProperty("logisticsCost")
    public void setLogisticsCost(Integer logisticsCost) {
        this.logisticsCost = logisticsCost;
    }

    @JsonProperty("lossInsuranceCost")
    public Integer getLossInsuranceCost() {
        return lossInsuranceCost;
    }

    @JsonProperty("lossInsuranceCost")
    public void setLossInsuranceCost(Integer lossInsuranceCost) {
        this.lossInsuranceCost = lossInsuranceCost;
    }

    @JsonProperty("lossInsuranceFactor")
    public Integer getLossInsuranceFactor() {
        return lossInsuranceFactor;
    }

    @JsonProperty("lossInsuranceFactor")
    public void setLossInsuranceFactor(Integer lossInsuranceFactor) {
        this.lossInsuranceFactor = lossInsuranceFactor;
    }

    @JsonProperty("machineLaborRateAdjustmentFactor")
    public Integer getMachineLaborRateAdjustmentFactor() {
        return machineLaborRateAdjustmentFactor;
    }

    @JsonProperty("machineLaborRateAdjustmentFactor")
    public void setMachineLaborRateAdjustmentFactor(Integer machineLaborRateAdjustmentFactor) {
        this.machineLaborRateAdjustmentFactor = machineLaborRateAdjustmentFactor;
    }

    @JsonProperty("machineLength")
    public Integer getMachineLength() {
        return machineLength;
    }

    @JsonProperty("machineLength")
    public void setMachineLength(Integer machineLength) {
        this.machineLength = machineLength;
    }

    @JsonProperty("machineLife")
    public Integer getMachineLife() {
        return machineLife;
    }

    @JsonProperty("machineLife")
    public void setMachineLife(Integer machineLife) {
        this.machineLife = machineLife;
    }

    @JsonProperty("machinePower")
    public Integer getMachinePower() {
        return machinePower;
    }

    @JsonProperty("machinePower")
    public void setMachinePower(Integer machinePower) {
        this.machinePower = machinePower;
    }

    @JsonProperty("machinePrice")
    public Integer getMachinePrice() {
        return machinePrice;
    }

    @JsonProperty("machinePrice")
    public void setMachinePrice(Integer machinePrice) {
        this.machinePrice = machinePrice;
    }

    @JsonProperty("machineUptime")
    public Integer getMachineUptime() {
        return machineUptime;
    }

    @JsonProperty("machineUptime")
    public void setMachineUptime(Integer machineUptime) {
        this.machineUptime = machineUptime;
    }

    @JsonProperty("machineWidth")
    public Integer getMachineWidth() {
        return machineWidth;
    }

    @JsonProperty("machineWidth")
    public void setMachineWidth(Integer machineWidth) {
        this.machineWidth = machineWidth;
    }

    @JsonProperty("maintenanceCost")
    public Integer getMaintenanceCost() {
        return maintenanceCost;
    }

    @JsonProperty("maintenanceCost")
    public void setMaintenanceCost(Integer maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    @JsonProperty("maintenanceDepartmentCost")
    public Integer getMaintenanceDepartmentCost() {
        return maintenanceDepartmentCost;
    }

    @JsonProperty("maintenanceDepartmentCost")
    public void setMaintenanceDepartmentCost(Integer maintenanceDepartmentCost) {
        this.maintenanceDepartmentCost = maintenanceDepartmentCost;
    }

    @JsonProperty("maintenanceSupportAllocation")
    public Integer getMaintenanceSupportAllocation() {
        return maintenanceSupportAllocation;
    }

    @JsonProperty("maintenanceSupportAllocation")
    public void setMaintenanceSupportAllocation(Integer maintenanceSupportAllocation) {
        this.maintenanceSupportAllocation = maintenanceSupportAllocation;
    }

    @JsonProperty("maintenanceSupportRate")
    public Integer getMaintenanceSupportRate() {
        return maintenanceSupportRate;
    }

    @JsonProperty("maintenanceSupportRate")
    public void setMaintenanceSupportRate(Integer maintenanceSupportRate) {
        this.maintenanceSupportRate = maintenanceSupportRate;
    }

    @JsonProperty("margin")
    public Integer getMargin() {
        return margin;
    }

    @JsonProperty("margin")
    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    @JsonProperty("marginPercent")
    public Integer getMarginPercent() {
        return marginPercent;
    }

    @JsonProperty("marginPercent")
    public void setMarginPercent(Integer marginPercent) {
        this.marginPercent = marginPercent;
    }

    @JsonProperty("materialCost")
    public Integer getMaterialCost() {
        return materialCost;
    }

    @JsonProperty("materialCost")
    public void setMaterialCost(Integer materialCost) {
        this.materialCost = materialCost;
    }

    @JsonProperty("materialName")
    public String getMaterialName() {
        return materialName;
    }

    @JsonProperty("materialName")
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @JsonProperty("materialOverheadCost")
    public Integer getMaterialOverheadCost() {
        return materialOverheadCost;
    }

    @JsonProperty("materialOverheadCost")
    public void setMaterialOverheadCost(Integer materialOverheadCost) {
        this.materialOverheadCost = materialOverheadCost;
    }

    @JsonProperty("materialOverheadPercent")
    public Integer getMaterialOverheadPercent() {
        return materialOverheadPercent;
    }

    @JsonProperty("materialOverheadPercent")
    public void setMaterialOverheadPercent(Integer materialOverheadPercent) {
        this.materialOverheadPercent = materialOverheadPercent;
    }

    @JsonProperty("materialStockFormName")
    public String getMaterialStockFormName() {
        return materialStockFormName;
    }

    @JsonProperty("materialStockFormName")
    public void setMaterialStockFormName(String materialStockFormName) {
        this.materialStockFormName = materialStockFormName;
    }

    @JsonProperty("materialStockName")
    public String getMaterialStockName() {
        return materialStockName;
    }

    @JsonProperty("materialStockName")
    public void setMaterialStockName(String materialStockName) {
        this.materialStockName = materialStockName;
    }

    @JsonProperty("materialUnitCost")
    public Integer getMaterialUnitCost() {
        return materialUnitCost;
    }

    @JsonProperty("materialUnitCost")
    public void setMaterialUnitCost(Integer materialUnitCost) {
        this.materialUnitCost = materialUnitCost;
    }

    @JsonProperty("materialYield")
    public Integer getMaterialYield() {
        return materialYield;
    }

    @JsonProperty("materialYield")
    public void setMaterialYield(Integer materialYield) {
        this.materialYield = materialYield;
    }

    @JsonProperty("nonProductionFootprintFactor")
    public Integer getNonProductionFootprintFactor() {
        return nonProductionFootprintFactor;
    }

    @JsonProperty("nonProductionFootprintFactor")
    public void setNonProductionFootprintFactor(Integer nonProductionFootprintFactor) {
        this.nonProductionFootprintFactor = nonProductionFootprintFactor;
    }

    @JsonProperty("notSupportedGcdsCount")
    public Integer getNotSupportedGcdsCount() {
        return notSupportedGcdsCount;
    }

    @JsonProperty("notSupportedGcdsCount")
    public void setNotSupportedGcdsCount(Integer notSupportedGcdsCount) {
        this.notSupportedGcdsCount = notSupportedGcdsCount;
    }

    @JsonProperty("numOperators")
    public Integer getNumOperators() {
        return numOperators;
    }

    @JsonProperty("numOperators")
    public void setNumOperators(Integer numOperators) {
        this.numOperators = numOperators;
    }

    @JsonProperty("numPartsPerSheet")
    public Integer getNumPartsPerSheet() {
        return numPartsPerSheet;
    }

    @JsonProperty("numPartsPerSheet")
    public void setNumPartsPerSheet(Integer numPartsPerSheet) {
        this.numPartsPerSheet = numPartsPerSheet;
    }

    @JsonProperty("numScrapParts")
    public Integer getNumScrapParts() {
        return numScrapParts;
    }

    @JsonProperty("numScrapParts")
    public void setNumScrapParts(Integer numScrapParts) {
        this.numScrapParts = numScrapParts;
    }

    @JsonProperty("numScrapPartsDownStream")
    public Integer getNumScrapPartsDownStream() {
        return numScrapPartsDownStream;
    }

    @JsonProperty("numScrapPartsDownStream")
    public void setNumScrapPartsDownStream(Integer numScrapPartsDownStream) {
        this.numScrapPartsDownStream = numScrapPartsDownStream;
    }

    @JsonProperty("numberOfParts")
    public Integer getNumberOfParts() {
        return numberOfParts;
    }

    @JsonProperty("numberOfParts")
    public void setNumberOfParts(Integer numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    @JsonProperty("otherDirectCosts")
    public Integer getOtherDirectCosts() {
        return otherDirectCosts;
    }

    @JsonProperty("otherDirectCosts")
    public void setOtherDirectCosts(Integer otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
    }

    @JsonProperty("overheadRate")
    public Integer getOverheadRate() {
        return overheadRate;
    }

    @JsonProperty("overheadRate")
    public void setOverheadRate(Integer overheadRate) {
        this.overheadRate = overheadRate;
    }

    @JsonProperty("partsPerHour")
    public Integer getPartsPerHour() {
        return partsPerHour;
    }

    @JsonProperty("partsPerHour")
    public void setPartsPerHour(Integer partsPerHour) {
        this.partsPerHour = partsPerHour;
    }

    @JsonProperty("periodOverhead")
    public Integer getPeriodOverhead() {
        return periodOverhead;
    }

    @JsonProperty("periodOverhead")
    public void setPeriodOverhead(Integer periodOverhead) {
        this.periodOverhead = periodOverhead;
    }

    @JsonProperty("pieceAndPeriod")
    public Integer getPieceAndPeriod() {
        return pieceAndPeriod;
    }

    @JsonProperty("pieceAndPeriod")
    public void setPieceAndPeriod(Integer pieceAndPeriod) {
        this.pieceAndPeriod = pieceAndPeriod;
    }

    @JsonProperty("pieceCost")
    public Integer getPieceCost() {
        return pieceCost;
    }

    @JsonProperty("pieceCost")
    public void setPieceCost(Integer pieceCost) {
        this.pieceCost = pieceCost;
    }

    @JsonProperty("plantLaborRateAdjustmentFactor")
    public Integer getPlantLaborRateAdjustmentFactor() {
        return plantLaborRateAdjustmentFactor;
    }

    @JsonProperty("plantLaborRateAdjustmentFactor")
    public void setPlantLaborRateAdjustmentFactor(Integer plantLaborRateAdjustmentFactor) {
        this.plantLaborRateAdjustmentFactor = plantLaborRateAdjustmentFactor;
    }

    @JsonProperty("processRoutingName")
    public String getProcessRoutingName() {
        return processRoutingName;
    }

    @JsonProperty("processRoutingName")
    public void setProcessRoutingName(String processRoutingName) {
        this.processRoutingName = processRoutingName;
    }

    @JsonProperty("productionDaysPerYear")
    public Integer getProductionDaysPerYear() {
        return productionDaysPerYear;
    }

    @JsonProperty("productionDaysPerYear")
    public void setProductionDaysPerYear(Integer productionDaysPerYear) {
        this.productionDaysPerYear = productionDaysPerYear;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public void setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
    }

    @JsonProperty("programmingCost")
    public Integer getProgrammingCost() {
        return programmingCost;
    }

    @JsonProperty("programmingCost")
    public void setProgrammingCost(Integer programmingCost) {
        this.programmingCost = programmingCost;
    }

    @JsonProperty("programmingCostPerPart")
    public Integer getProgrammingCostPerPart() {
        return programmingCostPerPart;
    }

    @JsonProperty("programmingCostPerPart")
    public void setProgrammingCostPerPart(Integer programmingCostPerPart) {
        this.programmingCostPerPart = programmingCostPerPart;
    }

    @JsonProperty("purchasingDepartmentCost")
    public Integer getPurchasingDepartmentCost() {
        return purchasingDepartmentCost;
    }

    @JsonProperty("purchasingDepartmentCost")
    public void setPurchasingDepartmentCost(Integer purchasingDepartmentCost) {
        this.purchasingDepartmentCost = purchasingDepartmentCost;
    }

    @JsonProperty("purchasingPowerFactor")
    public Integer getPurchasingPowerFactor() {
        return purchasingPowerFactor;
    }

    @JsonProperty("purchasingPowerFactor")
    public void setPurchasingPowerFactor(Integer purchasingPowerFactor) {
        this.purchasingPowerFactor = purchasingPowerFactor;
    }

    @JsonProperty("purchasingSupportAllocation")
    public Integer getPurchasingSupportAllocation() {
        return purchasingSupportAllocation;
    }

    @JsonProperty("purchasingSupportAllocation")
    public void setPurchasingSupportAllocation(Integer purchasingSupportAllocation) {
        this.purchasingSupportAllocation = purchasingSupportAllocation;
    }

    @JsonProperty("purchasingSupportRate")
    public Integer getPurchasingSupportRate() {
        return purchasingSupportRate;
    }

    @JsonProperty("purchasingSupportRate")
    public void setPurchasingSupportRate(Integer purchasingSupportRate) {
        this.purchasingSupportRate = purchasingSupportRate;
    }

    @JsonProperty("qualityDepartmentCost")
    public Integer getQualityDepartmentCost() {
        return qualityDepartmentCost;
    }

    @JsonProperty("qualityDepartmentCost")
    public void setQualityDepartmentCost(Integer qualityDepartmentCost) {
        this.qualityDepartmentCost = qualityDepartmentCost;
    }

    @JsonProperty("qualitySupportAllocation")
    public Integer getQualitySupportAllocation() {
        return qualitySupportAllocation;
    }

    @JsonProperty("qualitySupportAllocation")
    public void setQualitySupportAllocation(Integer qualitySupportAllocation) {
        this.qualitySupportAllocation = qualitySupportAllocation;
    }

    @JsonProperty("qualitySupportRate")
    public Integer getQualitySupportRate() {
        return qualitySupportRate;
    }

    @JsonProperty("qualitySupportRate")
    public void setQualitySupportRate(Integer qualitySupportRate) {
        this.qualitySupportRate = qualitySupportRate;
    }

    @JsonProperty("rentCost")
    public Integer getRentCost() {
        return rentCost;
    }

    @JsonProperty("rentCost")
    public void setRentCost(Integer rentCost) {
        this.rentCost = rentCost;
    }

    @JsonProperty("rentRate")
    public Integer getRentRate() {
        return rentRate;
    }

    @JsonProperty("rentRate")
    public void setRentRate(Integer rentRate) {
        this.rentRate = rentRate;
    }

    @JsonProperty("roughLength")
    public Integer getRoughLength() {
        return roughLength;
    }

    @JsonProperty("roughLength")
    public void setRoughLength(Integer roughLength) {
        this.roughLength = roughLength;
    }

    @JsonProperty("roughMass")
    public Integer getRoughMass() {
        return roughMass;
    }

    @JsonProperty("roughMass")
    public void setRoughMass(Integer roughMass) {
        this.roughMass = roughMass;
    }

    @JsonProperty("salvageValue")
    public Integer getSalvageValue() {
        return salvageValue;
    }

    @JsonProperty("salvageValue")
    public void setSalvageValue(Integer salvageValue) {
        this.salvageValue = salvageValue;
    }

    @JsonProperty("salvageValueFactor")
    public Integer getSalvageValueFactor() {
        return salvageValueFactor;
    }

    @JsonProperty("salvageValueFactor")
    public void setSalvageValueFactor(Integer salvageValueFactor) {
        this.salvageValueFactor = salvageValueFactor;
    }

    @JsonProperty("scrapMass")
    public Integer getScrapMass() {
        return scrapMass;
    }

    @JsonProperty("scrapMass")
    public void setScrapMass(Integer scrapMass) {
        this.scrapMass = scrapMass;
    }

    @JsonProperty("scrapPartCredit")
    public Integer getScrapPartCredit() {
        return scrapPartCredit;
    }

    @JsonProperty("scrapPartCredit")
    public void setScrapPartCredit(Integer scrapPartCredit) {
        this.scrapPartCredit = scrapPartCredit;
    }

    @JsonProperty("setupCostPerCore")
    public Integer getSetupCostPerCore() {
        return setupCostPerCore;
    }

    @JsonProperty("setupCostPerCore")
    public void setSetupCostPerCore(Integer setupCostPerCore) {
        this.setupCostPerCore = setupCostPerCore;
    }

    @JsonProperty("setupCostPerPart")
    public Integer getSetupCostPerPart() {
        return setupCostPerPart;
    }

    @JsonProperty("setupCostPerPart")
    public void setSetupCostPerPart(Integer setupCostPerPart) {
        this.setupCostPerPart = setupCostPerPart;
    }

    @JsonProperty("sgaCost")
    public Integer getSgaCost() {
        return sgaCost;
    }

    @JsonProperty("sgaCost")
    public void setSgaCost(Integer sgaCost) {
        this.sgaCost = sgaCost;
    }

    @JsonProperty("sgaPercent")
    public Integer getSgaPercent() {
        return sgaPercent;
    }

    @JsonProperty("sgaPercent")
    public void setSgaPercent(Integer sgaPercent) {
        this.sgaPercent = sgaPercent;
    }

    @JsonProperty("shiftsPerDay")
    public Integer getShiftsPerDay() {
        return shiftsPerDay;
    }

    @JsonProperty("shiftsPerDay")
    public void setShiftsPerDay(Integer shiftsPerDay) {
        this.shiftsPerDay = shiftsPerDay;
    }

    @JsonProperty("skillLevel")
    public Integer getSkillLevel() {
        return skillLevel;
    }

    @JsonProperty("skillLevel")
    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    @JsonProperty("stockPropertyHeight")
    public Integer getStockPropertyHeight() {
        return stockPropertyHeight;
    }

    @JsonProperty("stockPropertyHeight")
    public void setStockPropertyHeight(Integer stockPropertyHeight) {
        this.stockPropertyHeight = stockPropertyHeight;
    }

    @JsonProperty("stockPropertyInsideDia")
    public Integer getStockPropertyInsideDia() {
        return stockPropertyInsideDia;
    }

    @JsonProperty("stockPropertyInsideDia")
    public void setStockPropertyInsideDia(Integer stockPropertyInsideDia) {
        this.stockPropertyInsideDia = stockPropertyInsideDia;
    }

    @JsonProperty("stockPropertyLength")
    public Integer getStockPropertyLength() {
        return stockPropertyLength;
    }

    @JsonProperty("stockPropertyLength")
    public void setStockPropertyLength(Integer stockPropertyLength) {
        this.stockPropertyLength = stockPropertyLength;
    }

    @JsonProperty("stockPropertyOutsideDia")
    public Integer getStockPropertyOutsideDia() {
        return stockPropertyOutsideDia;
    }

    @JsonProperty("stockPropertyOutsideDia")
    public void setStockPropertyOutsideDia(Integer stockPropertyOutsideDia) {
        this.stockPropertyOutsideDia = stockPropertyOutsideDia;
    }

    @JsonProperty("stockPropertyThickness")
    public Integer getStockPropertyThickness() {
        return stockPropertyThickness;
    }

    @JsonProperty("stockPropertyThickness")
    public void setStockPropertyThickness(Integer stockPropertyThickness) {
        this.stockPropertyThickness = stockPropertyThickness;
    }

    @JsonProperty("stockPropertyWallThickness")
    public Integer getStockPropertyWallThickness() {
        return stockPropertyWallThickness;
    }

    @JsonProperty("stockPropertyWallThickness")
    public void setStockPropertyWallThickness(Integer stockPropertyWallThickness) {
        this.stockPropertyWallThickness = stockPropertyWallThickness;
    }

    @JsonProperty("stockPropertyWidth")
    public Integer getStockPropertyWidth() {
        return stockPropertyWidth;
    }

    @JsonProperty("stockPropertyWidth")
    public void setStockPropertyWidth(Integer stockPropertyWidth) {
        this.stockPropertyWidth = stockPropertyWidth;
    }

    @JsonProperty("stripNestingPitch")
    public Integer getStripNestingPitch() {
        return stripNestingPitch;
    }

    @JsonProperty("stripNestingPitch")
    public void setStripNestingPitch(Integer stripNestingPitch) {
        this.stripNestingPitch = stripNestingPitch;
    }

    @JsonProperty("suppliesCost")
    public Integer getSuppliesCost() {
        return suppliesCost;
    }

    @JsonProperty("suppliesCost")
    public void setSuppliesCost(Integer suppliesCost) {
        this.suppliesCost = suppliesCost;
    }

    @JsonProperty("supportAllocation")
    public Integer getSupportAllocation() {
        return supportAllocation;
    }

    @JsonProperty("supportAllocation")
    public void setSupportAllocation(Integer supportAllocation) {
        this.supportAllocation = supportAllocation;
    }

    @JsonProperty("supportServicesCost")
    public Integer getSupportServicesCost() {
        return supportServicesCost;
    }

    @JsonProperty("supportServicesCost")
    public void setSupportServicesCost(Integer supportServicesCost) {
        this.supportServicesCost = supportServicesCost;
    }

    @JsonProperty("toolCribDepartmentCost")
    public Integer getToolCribDepartmentCost() {
        return toolCribDepartmentCost;
    }

    @JsonProperty("toolCribDepartmentCost")
    public void setToolCribDepartmentCost(Integer toolCribDepartmentCost) {
        this.toolCribDepartmentCost = toolCribDepartmentCost;
    }

    @JsonProperty("toolCribSupportAllocation")
    public Integer getToolCribSupportAllocation() {
        return toolCribSupportAllocation;
    }

    @JsonProperty("toolCribSupportAllocation")
    public void setToolCribSupportAllocation(Integer toolCribSupportAllocation) {
        this.toolCribSupportAllocation = toolCribSupportAllocation;
    }

    @JsonProperty("toolCribSupportRate")
    public Integer getToolCribSupportRate() {
        return toolCribSupportRate;
    }

    @JsonProperty("toolCribSupportRate")
    public void setToolCribSupportRate(Integer toolCribSupportRate) {
        this.toolCribSupportRate = toolCribSupportRate;
    }

    @JsonProperty("toolingCostPerPart")
    public Integer getToolingCostPerPart() {
        return toolingCostPerPart;
    }

    @JsonProperty("toolingCostPerPart")
    public void setToolingCostPerPart(Integer toolingCostPerPart) {
        this.toolingCostPerPart = toolingCostPerPart;
    }

    @JsonProperty("totalCost")
    public Integer getTotalCost() {
        return totalCost;
    }

    @JsonProperty("totalCost")
    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    @JsonProperty("totalMachineCost")
    public Integer getTotalMachineCost() {
        return totalMachineCost;
    }

    @JsonProperty("totalMachineCost")
    public void setTotalMachineCost(Integer totalMachineCost) {
        this.totalMachineCost = totalMachineCost;
    }

    @JsonProperty("totalProductionVolume")
    public Integer getTotalProductionVolume() {
        return totalProductionVolume;
    }

    @JsonProperty("totalProductionVolume")
    public void setTotalProductionVolume(Integer totalProductionVolume) {
        this.totalProductionVolume = totalProductionVolume;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    @JsonProperty("useComputedOverheadRate")
    public Boolean getUseComputedOverheadRate() {
        return useComputedOverheadRate;
    }

    @JsonProperty("useComputedOverheadRate")
    public void setUseComputedOverheadRate(Boolean useComputedOverheadRate) {
        this.useComputedOverheadRate = useComputedOverheadRate;
    }

    @JsonProperty("useIndirectOverheadPercentage")
    public Boolean getUseIndirectOverheadPercentage() {
        return useIndirectOverheadPercentage;
    }

    @JsonProperty("useIndirectOverheadPercentage")
    public void setUseIndirectOverheadPercentage(Boolean useIndirectOverheadPercentage) {
        this.useIndirectOverheadPercentage = useIndirectOverheadPercentage;
    }

    @JsonProperty("utilitiesCost")
    public Integer getUtilitiesCost() {
        return utilitiesCost;
    }

    @JsonProperty("utilitiesCost")
    public void setUtilitiesCost(Integer utilitiesCost) {
        this.utilitiesCost = utilitiesCost;
    }

    @JsonProperty("utilization")
    public Integer getUtilization() {
        return utilization;
    }

    @JsonProperty("utilization")
    public void setUtilization(Integer utilization) {
        this.utilization = utilization;
    }

    @JsonProperty("utilizationWithAddendum")
    public Integer getUtilizationWithAddendum() {
        return utilizationWithAddendum;
    }

    @JsonProperty("utilizationWithAddendum")
    public void setUtilizationWithAddendum(Integer utilizationWithAddendum) {
        this.utilizationWithAddendum = utilizationWithAddendum;
    }

    @JsonProperty("utilizationWithoutAddendum")
    public Integer getUtilizationWithoutAddendum() {
        return utilizationWithoutAddendum;
    }

    @JsonProperty("utilizationWithoutAddendum")
    public void setUtilizationWithoutAddendum(Integer utilizationWithoutAddendum) {
        this.utilizationWithoutAddendum = utilizationWithoutAddendum;
    }

    @JsonProperty("virtualMaterialStockName")
    public String getVirtualMaterialStockName() {
        return virtualMaterialStockName;
    }

    @JsonProperty("virtualMaterialStockName")
    public void setVirtualMaterialStockName(String virtualMaterialStockName) {
        this.virtualMaterialStockName = virtualMaterialStockName;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonProperty("workCenterCapacity")
    public Integer getWorkCenterCapacity() {
        return workCenterCapacity;
    }

    @JsonProperty("workCenterCapacity")
    public void setWorkCenterCapacity(Integer workCenterCapacity) {
        this.workCenterCapacity = workCenterCapacity;
    }

    @JsonProperty("workCenterFootprint")
    public Integer getWorkCenterFootprint() {
        return workCenterFootprint;
    }

    @JsonProperty("workCenterFootprint")
    public void setWorkCenterFootprint(Integer workCenterFootprint) {
        this.workCenterFootprint = workCenterFootprint;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}