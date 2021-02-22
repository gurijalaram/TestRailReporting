package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioAnalysis {
    private Boolean addFacilityCostToDirOverhead;
    private Integer additionalAmortizedInvestment;
    private Integer additionalDirectCosts;
    private Integer amortizedInvestment;
    private Integer annualCost;
    private Integer annualEarnedMachineHours;
    private Integer annualMaintenanceFactor;
    private Integer annualVolume;
    private Integer batchCost;
    private Integer batchSetupTime;
    private Integer batchSize;
    private Integer cadSerLength;
    private Integer cadSerWidth;
    private Integer capitalInvestment;
    private String costingStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    private Integer cycleTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private Integer depreciationCost;
    private Integer dfmRisk;
    private Integer directOverheadCost;
    private Integer directOverheadRate;
    private Integer dtcMessagesCount;
    private Integer elapsedTime;
    private Integer electricityRate;
    private Integer energyCost;
    private Integer engineeringDepartmentCost;
    private Integer engineeringSupportAllocation;
    private Integer engineeringSupportRate;
    private Integer expendableToolingCost;
    private Integer expendableToolingCostPerCore;
    private Integer expendableToolingCostPerPart;
    private Integer extraCosts;
    private Integer facilityDirectOverheadCost;
    private Integer facilityElectricityCost;
    private Integer facilityElectricityFactor;
    private Integer facilityHeatAndGasCost;
    private Integer facilityHeatAndGasFactor;
    private Integer facilityIndirectOverheadCost;
    private Integer facilityWaterCost;
    private Integer facilityWaterFactor;
    private Integer failedGcdsCount;
    private Integer failuresWarningsCount;
    private Integer finalYield;
    private Integer finishMass;
    private Integer fireInsuranceCost;
    private Integer fireInsuranceFactor;
    private Integer fixtureCost;
    private Integer fixtureCostPerPart;
    private Integer footprintAllowanceFactor;
    private Integer fullyBurdenedCost;
    private Integer gasRate;
    private Integer gcdWithTolerancesCount;
    private Integer goodPartYield;
    private Integer hardToolingCost;
    private Integer hardToolingCostPerPart;
    private Integer height;
    private Integer hoursPerShift;
    private String identity;
    private Integer imputedInterestCost;
    private Integer imputedInterestRate;
    private Integer indirectOverheadPercent;
    private Integer indirectOverheadRate;
    private Integer installationCost;
    private Integer installationFactor;
    private Integer insuranceCost;
    private Integer laborCost;
    private Integer laborRate;
    private Integer laborTime;
    private String lastCosted;
    private Integer length;
    private Integer liabilityInsuranceCost;
    private Integer liabilityInsuranceFactor;
    private Integer lifetimeCost;
    private Integer logisticsCost;
    private Integer lossInsuranceCost;
    private Integer lossInsuranceFactor;
    private Integer machineLaborRateAdjustmentFactor;
    private Integer machineLength;
    private Integer machineLife;
    private Integer machinePower;
    private Integer machinePrice;
    private Integer machineUptime;
    private Integer machineWidth;
    private Integer maintenanceCost;
    private Integer maintenanceDepartmentCost;
    private Integer maintenanceSupportAllocation;
    private Integer maintenanceSupportRate;
    private Integer margin;
    private Integer marginPercent;
    private Integer materialCost;
    private String materialName;
    private Integer materialOverheadCost;
    private Integer materialOverheadPercent;
    private String materialStockFormName;
    private String materialStockName;
    private Integer materialUnitCost;
    private Integer materialYield;
    private Integer nonProductionFootprintFactor;
    private Integer notSupportedGcdsCount;
    private Integer numOperators;
    private Integer numPartsPerSheet;
    private Integer numScrapParts;
    private Integer numScrapPartsDownStream;
    private Integer numberOfParts;
    private Integer otherDirectCosts;
    private Integer overheadRate;
    private Integer partsPerHour;
    private Integer periodOverhead;
    private Integer pieceAndPeriod;
    private Integer pieceCost;
    private Integer plantLaborRateAdjustmentFactor;
    private String processRoutingName;
    private Integer productionDaysPerYear;
    private Integer productionLife;
    private Integer programmingCost;
    private Integer programmingCostPerPart;
    private Integer purchasingDepartmentCost;
    private Integer purchasingPowerFactor;
    private Integer purchasingSupportAllocation;
    private Integer purchasingSupportRate;
    private Integer qualityDepartmentCost;
    private Integer qualitySupportAllocation;
    private Integer qualitySupportRate;
    private Integer rentCost;
    private Integer rentRate;
    private Integer roughLength;
    private Integer roughMass;
    private Integer salvageValue;
    private Integer salvageValueFactor;
    private Integer scrapMass;
    private Integer scrapPartCredit;
    private Integer setupCostPerCore;
    private Integer setupCostPerPart;
    private Integer sgaCost;
    private Integer sgaPercent;
    private Integer shiftsPerDay;
    private Integer skillLevel;
    private Integer stockPropertyHeight;
    private Integer stockPropertyInsideDia;
    private Integer stockPropertyLength;
    private Integer stockPropertyOutsideDia;
    private Integer stockPropertyThickness;
    private Integer stockPropertyWallThickness;
    private Integer stockPropertyWidth;
    private Integer stripNestingPitch;
    private Integer suppliesCost;
    private Integer supportAllocation;
    private Integer supportServicesCost;
    private Integer toolCribDepartmentCost;
    private Integer toolCribSupportAllocation;
    private Integer toolCribSupportRate;
    private Integer toolingCostPerPart;
    private Integer totalCost;
    private Integer totalMachineCost;
    private Integer totalProductionVolume;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;
    private Boolean useComputedOverheadRate;
    private Boolean useIndirectOverheadPercentage;
    private Integer utilitiesCost;
    private Integer utilization;
    private Integer utilizationWithAddendum;
    private Integer utilizationWithoutAddendum;
    private String virtualMaterialStockName;
    private Integer width;
    private Integer workCenterCapacity;
    private Integer workCenterFootprint;

    public Boolean getAddFacilityCostToDirOverhead() {
        return addFacilityCostToDirOverhead;
    }

    public ScenarioAnalysis setAddFacilityCostToDirOverhead(Boolean addFacilityCostToDirOverhead) {
        this.addFacilityCostToDirOverhead = addFacilityCostToDirOverhead;
        return this;
    }

    public Integer getAdditionalAmortizedInvestment() {
        return additionalAmortizedInvestment;
    }

    public ScenarioAnalysis setAdditionalAmortizedInvestment(Integer additionalAmortizedInvestment) {
        this.additionalAmortizedInvestment = additionalAmortizedInvestment;
        return this;
    }

    public Integer getAdditionalDirectCosts() {
        return additionalDirectCosts;
    }

    public ScenarioAnalysis setAdditionalDirectCosts(Integer additionalDirectCosts) {
        this.additionalDirectCosts = additionalDirectCosts;
        return this;
    }

    public Integer getAmortizedInvestment() {
        return amortizedInvestment;
    }

    public ScenarioAnalysis setAmortizedInvestment(Integer amortizedInvestment) {
        this.amortizedInvestment = amortizedInvestment;
        return this;
    }

    public Integer getAnnualCost() {
        return annualCost;
    }

    public ScenarioAnalysis setAnnualCost(Integer annualCost) {
        this.annualCost = annualCost;
        return this;
    }

    public Integer getAnnualEarnedMachineHours() {
        return annualEarnedMachineHours;
    }

    public ScenarioAnalysis setAnnualEarnedMachineHours(Integer annualEarnedMachineHours) {
        this.annualEarnedMachineHours = annualEarnedMachineHours;
        return this;
    }

    public Integer getAnnualMaintenanceFactor() {
        return annualMaintenanceFactor;
    }

    public ScenarioAnalysis setAnnualMaintenanceFactor(Integer annualMaintenanceFactor) {
        this.annualMaintenanceFactor = annualMaintenanceFactor;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public ScenarioAnalysis setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Integer getBatchCost() {
        return batchCost;
    }

    public ScenarioAnalysis setBatchCost(Integer batchCost) {
        this.batchCost = batchCost;
        return this;
    }

    public Integer getBatchSetupTime() {
        return batchSetupTime;
    }

    public ScenarioAnalysis setBatchSetupTime(Integer batchSetupTime) {
        this.batchSetupTime = batchSetupTime;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public ScenarioAnalysis setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Integer getCadSerLength() {
        return cadSerLength;
    }

    public ScenarioAnalysis setCadSerLength(Integer cadSerLength) {
        this.cadSerLength = cadSerLength;
        return this;
    }

    public Integer getCadSerWidth() {
        return cadSerWidth;
    }

    public ScenarioAnalysis setCadSerWidth(Integer cadSerWidth) {
        this.cadSerWidth = cadSerWidth;
        return this;
    }

    public Integer getCapitalInvestment() {
        return capitalInvestment;
    }

    public ScenarioAnalysis setCapitalInvestment(Integer capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
        return this;
    }

    public String getCostingStatus() {
        return costingStatus;
    }

    public ScenarioAnalysis setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ScenarioAnalysis setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ScenarioAnalysis setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ScenarioAnalysis setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public Integer getCycleTime() {
        return cycleTime;
    }

    public ScenarioAnalysis setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ScenarioAnalysis setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public ScenarioAnalysis setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public ScenarioAnalysis setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    public Integer getDepreciationCost() {
        return depreciationCost;
    }

    public ScenarioAnalysis setDepreciationCost(Integer depreciationCost) {
        this.depreciationCost = depreciationCost;
        return this;
    }

    public Integer getDfmRisk() {
        return dfmRisk;
    }

    public ScenarioAnalysis setDfmRisk(Integer dfmRisk) {
        this.dfmRisk = dfmRisk;
        return this;
    }

    public Integer getDirectOverheadCost() {
        return directOverheadCost;
    }

    public ScenarioAnalysis setDirectOverheadCost(Integer directOverheadCost) {
        this.directOverheadCost = directOverheadCost;
        return this;
    }

    public Integer getDirectOverheadRate() {
        return directOverheadRate;
    }

    public ScenarioAnalysis setDirectOverheadRate(Integer directOverheadRate) {
        this.directOverheadRate = directOverheadRate;
        return this;
    }

    public Integer getDtcMessagesCount() {
        return dtcMessagesCount;
    }

    public ScenarioAnalysis setDtcMessagesCount(Integer dtcMessagesCount) {
        this.dtcMessagesCount = dtcMessagesCount;
        return this;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public ScenarioAnalysis setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public Integer getElectricityRate() {
        return electricityRate;
    }

    public ScenarioAnalysis setElectricityRate(Integer electricityRate) {
        this.electricityRate = electricityRate;
        return this;
    }

    public Integer getEnergyCost() {
        return energyCost;
    }

    public ScenarioAnalysis setEnergyCost(Integer energyCost) {
        this.energyCost = energyCost;
        return this;
    }

    public Integer getEngineeringDepartmentCost() {
        return engineeringDepartmentCost;
    }

    public ScenarioAnalysis setEngineeringDepartmentCost(Integer engineeringDepartmentCost) {
        this.engineeringDepartmentCost = engineeringDepartmentCost;
        return this;
    }

    public Integer getEngineeringSupportAllocation() {
        return engineeringSupportAllocation;
    }

    public ScenarioAnalysis setEngineeringSupportAllocation(Integer engineeringSupportAllocation) {
        this.engineeringSupportAllocation = engineeringSupportAllocation;
        return this;
    }

    public Integer getEngineeringSupportRate() {
        return engineeringSupportRate;
    }

    public ScenarioAnalysis setEngineeringSupportRate(Integer engineeringSupportRate) {
        this.engineeringSupportRate = engineeringSupportRate;
        return this;
    }

    public Integer getExpendableToolingCost() {
        return expendableToolingCost;
    }

    public ScenarioAnalysis setExpendableToolingCost(Integer expendableToolingCost) {
        this.expendableToolingCost = expendableToolingCost;
        return this;
    }

    public Integer getExpendableToolingCostPerCore() {
        return expendableToolingCostPerCore;
    }

    public ScenarioAnalysis setExpendableToolingCostPerCore(Integer expendableToolingCostPerCore) {
        this.expendableToolingCostPerCore = expendableToolingCostPerCore;
        return this;
    }

    public Integer getExpendableToolingCostPerPart() {
        return expendableToolingCostPerPart;
    }

    public ScenarioAnalysis setExpendableToolingCostPerPart(Integer expendableToolingCostPerPart) {
        this.expendableToolingCostPerPart = expendableToolingCostPerPart;
        return this;
    }

    public Integer getExtraCosts() {
        return extraCosts;
    }

    public ScenarioAnalysis setExtraCosts(Integer extraCosts) {
        this.extraCosts = extraCosts;
        return this;
    }

    public Integer getFacilityDirectOverheadCost() {
        return facilityDirectOverheadCost;
    }

    public ScenarioAnalysis setFacilityDirectOverheadCost(Integer facilityDirectOverheadCost) {
        this.facilityDirectOverheadCost = facilityDirectOverheadCost;
        return this;
    }

    public Integer getFacilityElectricityCost() {
        return facilityElectricityCost;
    }

    public ScenarioAnalysis setFacilityElectricityCost(Integer facilityElectricityCost) {
        this.facilityElectricityCost = facilityElectricityCost;
        return this;
    }

    public Integer getFacilityElectricityFactor() {
        return facilityElectricityFactor;
    }

    public ScenarioAnalysis setFacilityElectricityFactor(Integer facilityElectricityFactor) {
        this.facilityElectricityFactor = facilityElectricityFactor;
        return this;
    }

    public Integer getFacilityHeatAndGasCost() {
        return facilityHeatAndGasCost;
    }

    public ScenarioAnalysis setFacilityHeatAndGasCost(Integer facilityHeatAndGasCost) {
        this.facilityHeatAndGasCost = facilityHeatAndGasCost;
        return this;
    }

    public Integer getFacilityHeatAndGasFactor() {
        return facilityHeatAndGasFactor;
    }

    public ScenarioAnalysis setFacilityHeatAndGasFactor(Integer facilityHeatAndGasFactor) {
        this.facilityHeatAndGasFactor = facilityHeatAndGasFactor;
        return this;
    }

    public Integer getFacilityIndirectOverheadCost() {
        return facilityIndirectOverheadCost;
    }

    public ScenarioAnalysis setFacilityIndirectOverheadCost(Integer facilityIndirectOverheadCost) {
        this.facilityIndirectOverheadCost = facilityIndirectOverheadCost;
        return this;
    }

    public Integer getFacilityWaterCost() {
        return facilityWaterCost;
    }

    public ScenarioAnalysis setFacilityWaterCost(Integer facilityWaterCost) {
        this.facilityWaterCost = facilityWaterCost;
        return this;
    }

    public Integer getFacilityWaterFactor() {
        return facilityWaterFactor;
    }

    public ScenarioAnalysis setFacilityWaterFactor(Integer facilityWaterFactor) {
        this.facilityWaterFactor = facilityWaterFactor;
        return this;
    }

    public Integer getFailedGcdsCount() {
        return failedGcdsCount;
    }

    public ScenarioAnalysis setFailedGcdsCount(Integer failedGcdsCount) {
        this.failedGcdsCount = failedGcdsCount;
        return this;
    }

    public Integer getFailuresWarningsCount() {
        return failuresWarningsCount;
    }

    public ScenarioAnalysis setFailuresWarningsCount(Integer failuresWarningsCount) {
        this.failuresWarningsCount = failuresWarningsCount;
        return this;
    }

    public Integer getFinalYield() {
        return finalYield;
    }

    public ScenarioAnalysis setFinalYield(Integer finalYield) {
        this.finalYield = finalYield;
        return this;
    }

    public Integer getFinishMass() {
        return finishMass;
    }

    public ScenarioAnalysis setFinishMass(Integer finishMass) {
        this.finishMass = finishMass;
        return this;
    }

    public Integer getFireInsuranceCost() {
        return fireInsuranceCost;
    }

    public ScenarioAnalysis setFireInsuranceCost(Integer fireInsuranceCost) {
        this.fireInsuranceCost = fireInsuranceCost;
        return this;
    }

    public Integer getFireInsuranceFactor() {
        return fireInsuranceFactor;
    }

    public ScenarioAnalysis setFireInsuranceFactor(Integer fireInsuranceFactor) {
        this.fireInsuranceFactor = fireInsuranceFactor;
        return this;
    }

    public Integer getFixtureCost() {
        return fixtureCost;
    }

    public ScenarioAnalysis setFixtureCost(Integer fixtureCost) {
        this.fixtureCost = fixtureCost;
        return this;
    }

    public Integer getFixtureCostPerPart() {
        return fixtureCostPerPart;
    }

    public ScenarioAnalysis setFixtureCostPerPart(Integer fixtureCostPerPart) {
        this.fixtureCostPerPart = fixtureCostPerPart;
        return this;
    }

    public Integer getFootprintAllowanceFactor() {
        return footprintAllowanceFactor;
    }

    public ScenarioAnalysis setFootprintAllowanceFactor(Integer footprintAllowanceFactor) {
        this.footprintAllowanceFactor = footprintAllowanceFactor;
        return this;
    }

    public Integer getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public ScenarioAnalysis setFullyBurdenedCost(Integer fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    public Integer getGasRate() {
        return gasRate;
    }

    public ScenarioAnalysis setGasRate(Integer gasRate) {
        this.gasRate = gasRate;
        return this;
    }

    public Integer getGcdWithTolerancesCount() {
        return gcdWithTolerancesCount;
    }

    public ScenarioAnalysis setGcdWithTolerancesCount(Integer gcdWithTolerancesCount) {
        this.gcdWithTolerancesCount = gcdWithTolerancesCount;
        return this;
    }

    public Integer getGoodPartYield() {
        return goodPartYield;
    }

    public ScenarioAnalysis setGoodPartYield(Integer goodPartYield) {
        this.goodPartYield = goodPartYield;
        return this;
    }

    public Integer getHardToolingCost() {
        return hardToolingCost;
    }

    public ScenarioAnalysis setHardToolingCost(Integer hardToolingCost) {
        this.hardToolingCost = hardToolingCost;
        return this;
    }

    public Integer getHardToolingCostPerPart() {
        return hardToolingCostPerPart;
    }

    public ScenarioAnalysis setHardToolingCostPerPart(Integer hardToolingCostPerPart) {
        this.hardToolingCostPerPart = hardToolingCostPerPart;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public ScenarioAnalysis setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getHoursPerShift() {
        return hoursPerShift;
    }

    public ScenarioAnalysis setHoursPerShift(Integer hoursPerShift) {
        this.hoursPerShift = hoursPerShift;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ScenarioAnalysis setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Integer getImputedInterestCost() {
        return imputedInterestCost;
    }

    public ScenarioAnalysis setImputedInterestCost(Integer imputedInterestCost) {
        this.imputedInterestCost = imputedInterestCost;
        return this;
    }

    public Integer getImputedInterestRate() {
        return imputedInterestRate;
    }

    public ScenarioAnalysis setImputedInterestRate(Integer imputedInterestRate) {
        this.imputedInterestRate = imputedInterestRate;
        return this;
    }

    public Integer getIndirectOverheadPercent() {
        return indirectOverheadPercent;
    }

    public ScenarioAnalysis setIndirectOverheadPercent(Integer indirectOverheadPercent) {
        this.indirectOverheadPercent = indirectOverheadPercent;
        return this;
    }

    public Integer getIndirectOverheadRate() {
        return indirectOverheadRate;
    }

    public ScenarioAnalysis setIndirectOverheadRate(Integer indirectOverheadRate) {
        this.indirectOverheadRate = indirectOverheadRate;
        return this;
    }

    public Integer getInstallationCost() {
        return installationCost;
    }

    public ScenarioAnalysis setInstallationCost(Integer installationCost) {
        this.installationCost = installationCost;
        return this;
    }

    public Integer getInstallationFactor() {
        return installationFactor;
    }

    public ScenarioAnalysis setInstallationFactor(Integer installationFactor) {
        this.installationFactor = installationFactor;
        return this;
    }

    public Integer getInsuranceCost() {
        return insuranceCost;
    }

    public ScenarioAnalysis setInsuranceCost(Integer insuranceCost) {
        this.insuranceCost = insuranceCost;
        return this;
    }

    public Integer getLaborCost() {
        return laborCost;
    }

    public ScenarioAnalysis setLaborCost(Integer laborCost) {
        this.laborCost = laborCost;
        return this;
    }

    public Integer getLaborRate() {
        return laborRate;
    }

    public ScenarioAnalysis setLaborRate(Integer laborRate) {
        this.laborRate = laborRate;
        return this;
    }

    public Integer getLaborTime() {
        return laborTime;
    }

    public ScenarioAnalysis setLaborTime(Integer laborTime) {
        this.laborTime = laborTime;
        return this;
    }

    public String getLastCosted() {
        return lastCosted;
    }

    public ScenarioAnalysis setLastCosted(String lastCosted) {
        this.lastCosted = lastCosted;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public ScenarioAnalysis setLength(Integer length) {
        this.length = length;
        return this;
    }

    public Integer getLiabilityInsuranceCost() {
        return liabilityInsuranceCost;
    }

    public ScenarioAnalysis setLiabilityInsuranceCost(Integer liabilityInsuranceCost) {
        this.liabilityInsuranceCost = liabilityInsuranceCost;
        return this;
    }

    public Integer getLiabilityInsuranceFactor() {
        return liabilityInsuranceFactor;
    }

    public ScenarioAnalysis setLiabilityInsuranceFactor(Integer liabilityInsuranceFactor) {
        this.liabilityInsuranceFactor = liabilityInsuranceFactor;
        return this;
    }

    public Integer getLifetimeCost() {
        return lifetimeCost;
    }

    public ScenarioAnalysis setLifetimeCost(Integer lifetimeCost) {
        this.lifetimeCost = lifetimeCost;
        return this;
    }

    public Integer getLogisticsCost() {
        return logisticsCost;
    }

    public ScenarioAnalysis setLogisticsCost(Integer logisticsCost) {
        this.logisticsCost = logisticsCost;
        return this;
    }

    public Integer getLossInsuranceCost() {
        return lossInsuranceCost;
    }

    public ScenarioAnalysis setLossInsuranceCost(Integer lossInsuranceCost) {
        this.lossInsuranceCost = lossInsuranceCost;
        return this;
    }

    public Integer getLossInsuranceFactor() {
        return lossInsuranceFactor;
    }

    public ScenarioAnalysis setLossInsuranceFactor(Integer lossInsuranceFactor) {
        this.lossInsuranceFactor = lossInsuranceFactor;
        return this;
    }

    public Integer getMachineLaborRateAdjustmentFactor() {
        return machineLaborRateAdjustmentFactor;
    }

    public ScenarioAnalysis setMachineLaborRateAdjustmentFactor(Integer machineLaborRateAdjustmentFactor) {
        this.machineLaborRateAdjustmentFactor = machineLaborRateAdjustmentFactor;
        return this;
    }

    public Integer getMachineLength() {
        return machineLength;
    }

    public ScenarioAnalysis setMachineLength(Integer machineLength) {
        this.machineLength = machineLength;
        return this;
    }

    public Integer getMachineLife() {
        return machineLife;
    }

    public ScenarioAnalysis setMachineLife(Integer machineLife) {
        this.machineLife = machineLife;
        return this;
    }

    public Integer getMachinePower() {
        return machinePower;
    }

    public ScenarioAnalysis setMachinePower(Integer machinePower) {
        this.machinePower = machinePower;
        return this;
    }

    public Integer getMachinePrice() {
        return machinePrice;
    }

    public ScenarioAnalysis setMachinePrice(Integer machinePrice) {
        this.machinePrice = machinePrice;
        return this;
    }

    public Integer getMachineUptime() {
        return machineUptime;
    }

    public ScenarioAnalysis setMachineUptime(Integer machineUptime) {
        this.machineUptime = machineUptime;
        return this;
    }

    public Integer getMachineWidth() {
        return machineWidth;
    }

    public ScenarioAnalysis setMachineWidth(Integer machineWidth) {
        this.machineWidth = machineWidth;
        return this;
    }

    public Integer getMaintenanceCost() {
        return maintenanceCost;
    }

    public ScenarioAnalysis setMaintenanceCost(Integer maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
        return this;
    }

    public Integer getMaintenanceDepartmentCost() {
        return maintenanceDepartmentCost;
    }

    public ScenarioAnalysis setMaintenanceDepartmentCost(Integer maintenanceDepartmentCost) {
        this.maintenanceDepartmentCost = maintenanceDepartmentCost;
        return this;
    }

    public Integer getMaintenanceSupportAllocation() {
        return maintenanceSupportAllocation;
    }

    public ScenarioAnalysis setMaintenanceSupportAllocation(Integer maintenanceSupportAllocation) {
        this.maintenanceSupportAllocation = maintenanceSupportAllocation;
        return this;
    }

    public Integer getMaintenanceSupportRate() {
        return maintenanceSupportRate;
    }

    public ScenarioAnalysis setMaintenanceSupportRate(Integer maintenanceSupportRate) {
        this.maintenanceSupportRate = maintenanceSupportRate;
        return this;
    }

    public Integer getMargin() {
        return margin;
    }

    public ScenarioAnalysis setMargin(Integer margin) {
        this.margin = margin;
        return this;
    }

    public Integer getMarginPercent() {
        return marginPercent;
    }

    public ScenarioAnalysis setMarginPercent(Integer marginPercent) {
        this.marginPercent = marginPercent;
        return this;
    }

    public Integer getMaterialCost() {
        return materialCost;
    }

    public ScenarioAnalysis setMaterialCost(Integer materialCost) {
        this.materialCost = materialCost;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public ScenarioAnalysis setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public Integer getMaterialOverheadCost() {
        return materialOverheadCost;
    }

    public ScenarioAnalysis setMaterialOverheadCost(Integer materialOverheadCost) {
        this.materialOverheadCost = materialOverheadCost;
        return this;
    }

    public Integer getMaterialOverheadPercent() {
        return materialOverheadPercent;
    }

    public ScenarioAnalysis setMaterialOverheadPercent(Integer materialOverheadPercent) {
        this.materialOverheadPercent = materialOverheadPercent;
        return this;
    }

    public String getMaterialStockFormName() {
        return materialStockFormName;
    }

    public ScenarioAnalysis setMaterialStockFormName(String materialStockFormName) {
        this.materialStockFormName = materialStockFormName;
        return this;
    }

    public String getMaterialStockName() {
        return materialStockName;
    }

    public ScenarioAnalysis setMaterialStockName(String materialStockName) {
        this.materialStockName = materialStockName;
        return this;
    }


    public Integer getMaterialUnitCost() {
        return materialUnitCost;
    }


    public ScenarioAnalysis setMaterialUnitCost(Integer materialUnitCost) {
        this.materialUnitCost = materialUnitCost;
        return this;
    }

    public Integer getMaterialYield() {
        return materialYield;
    }

    public ScenarioAnalysis setMaterialYield(Integer materialYield) {
        this.materialYield = materialYield;
        return this;
    }

    public Integer getNonProductionFootprintFactor() {
        return nonProductionFootprintFactor;
    }

    public ScenarioAnalysis setNonProductionFootprintFactor(Integer nonProductionFootprintFactor) {
        this.nonProductionFootprintFactor = nonProductionFootprintFactor;
        return this;
    }

    public Integer getNotSupportedGcdsCount() {
        return notSupportedGcdsCount;
    }

    public ScenarioAnalysis setNotSupportedGcdsCount(Integer notSupportedGcdsCount) {
        this.notSupportedGcdsCount = notSupportedGcdsCount;
        return this;
    }

    public Integer getNumOperators() {
        return numOperators;
    }

    public ScenarioAnalysis setNumOperators(Integer numOperators) {
        this.numOperators = numOperators;
        return this;
    }

    public Integer getNumPartsPerSheet() {
        return numPartsPerSheet;
    }

    public ScenarioAnalysis setNumPartsPerSheet(Integer numPartsPerSheet) {
        this.numPartsPerSheet = numPartsPerSheet;
        return this;
    }

    public Integer getNumScrapParts() {
        return numScrapParts;
    }

    public ScenarioAnalysis setNumScrapParts(Integer numScrapParts) {
        this.numScrapParts = numScrapParts;
        return this;
    }

    public Integer getNumScrapPartsDownStream() {
        return numScrapPartsDownStream;
    }

    public ScenarioAnalysis setNumScrapPartsDownStream(Integer numScrapPartsDownStream) {
        this.numScrapPartsDownStream = numScrapPartsDownStream;
        return this;
    }

    public Integer getNumberOfParts() {
        return numberOfParts;
    }

    public ScenarioAnalysis setNumberOfParts(Integer numberOfParts) {
        this.numberOfParts = numberOfParts;
        return this;
    }

    public Integer getOtherDirectCosts() {
        return otherDirectCosts;
    }

    public ScenarioAnalysis setOtherDirectCosts(Integer otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
        return this;
    }

    public Integer getOverheadRate() {
        return overheadRate;
    }

    public ScenarioAnalysis setOverheadRate(Integer overheadRate) {
        this.overheadRate = overheadRate;
        return this;
    }

    public Integer getPartsPerHour() {
        return partsPerHour;
    }

    public ScenarioAnalysis setPartsPerHour(Integer partsPerHour) {
        this.partsPerHour = partsPerHour;
        return this;
    }

    public Integer getPeriodOverhead() {
        return periodOverhead;
    }

    public ScenarioAnalysis setPeriodOverhead(Integer periodOverhead) {
        this.periodOverhead = periodOverhead;
        return this;
    }

    public Integer getPieceAndPeriod() {
        return pieceAndPeriod;
    }

    public ScenarioAnalysis setPieceAndPeriod(Integer pieceAndPeriod) {
        this.pieceAndPeriod = pieceAndPeriod;
        return this;
    }

    public Integer getPieceCost() {
        return pieceCost;
    }

    public ScenarioAnalysis setPieceCost(Integer pieceCost) {
        this.pieceCost = pieceCost;
        return this;
    }

    public Integer getPlantLaborRateAdjustmentFactor() {
        return plantLaborRateAdjustmentFactor;
    }

    public ScenarioAnalysis setPlantLaborRateAdjustmentFactor(Integer plantLaborRateAdjustmentFactor) {
        this.plantLaborRateAdjustmentFactor = plantLaborRateAdjustmentFactor;
        return this;
    }

    public String getProcessRoutingName() {
        return processRoutingName;
    }

    public ScenarioAnalysis setProcessRoutingName(String processRoutingName) {
        this.processRoutingName = processRoutingName;
        return this;
    }

    public Integer getProductionDaysPerYear() {
        return productionDaysPerYear;
    }

    public ScenarioAnalysis setProductionDaysPerYear(Integer productionDaysPerYear) {
        this.productionDaysPerYear = productionDaysPerYear;
        return this;
    }

    public Integer getProductionLife() {
        return productionLife;
    }

    public ScenarioAnalysis setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public Integer getProgrammingCost() {
        return programmingCost;
    }

    public ScenarioAnalysis setProgrammingCost(Integer programmingCost) {
        this.programmingCost = programmingCost;
        return this;
    }

    public Integer getProgrammingCostPerPart() {
        return programmingCostPerPart;
    }

    public ScenarioAnalysis setProgrammingCostPerPart(Integer programmingCostPerPart) {
        this.programmingCostPerPart = programmingCostPerPart;
        return this;
    }

    public Integer getPurchasingDepartmentCost() {
        return purchasingDepartmentCost;
    }

    public ScenarioAnalysis setPurchasingDepartmentCost(Integer purchasingDepartmentCost) {
        this.purchasingDepartmentCost = purchasingDepartmentCost;
        return this;
    }

    public Integer getPurchasingPowerFactor() {
        return purchasingPowerFactor;
    }

    public ScenarioAnalysis setPurchasingPowerFactor(Integer purchasingPowerFactor) {
        this.purchasingPowerFactor = purchasingPowerFactor;
        return this;
    }

    public Integer getPurchasingSupportAllocation() {
        return purchasingSupportAllocation;
    }

    public ScenarioAnalysis setPurchasingSupportAllocation(Integer purchasingSupportAllocation) {
        this.purchasingSupportAllocation = purchasingSupportAllocation;
        return this;
    }

    public Integer getPurchasingSupportRate() {
        return purchasingSupportRate;
    }

    public ScenarioAnalysis setPurchasingSupportRate(Integer purchasingSupportRate) {
        this.purchasingSupportRate = purchasingSupportRate;
        return this;
    }

    public Integer getQualityDepartmentCost() {
        return qualityDepartmentCost;
    }

    public ScenarioAnalysis setQualityDepartmentCost(Integer qualityDepartmentCost) {
        this.qualityDepartmentCost = qualityDepartmentCost;
        return this;
    }

    public Integer getQualitySupportAllocation() {
        return qualitySupportAllocation;
    }

    public ScenarioAnalysis setQualitySupportAllocation(Integer qualitySupportAllocation) {
        this.qualitySupportAllocation = qualitySupportAllocation;
        return this;
    }

    public Integer getQualitySupportRate() {
        return qualitySupportRate;
    }

    public ScenarioAnalysis setQualitySupportRate(Integer qualitySupportRate) {
        this.qualitySupportRate = qualitySupportRate;
        return this;
    }

    public Integer getRentCost() {
        return rentCost;
    }

    public ScenarioAnalysis setRentCost(Integer rentCost) {
        this.rentCost = rentCost;
        return this;
    }

    public Integer getRentRate() {
        return rentRate;
    }

    public ScenarioAnalysis setRentRate(Integer rentRate) {
        this.rentRate = rentRate;
        return this;
    }

    public Integer getRoughLength() {
        return roughLength;
    }

    public ScenarioAnalysis setRoughLength(Integer roughLength) {
        this.roughLength = roughLength;
        return this;
    }

    public Integer getRoughMass() {
        return roughMass;
    }

    public ScenarioAnalysis setRoughMass(Integer roughMass) {
        this.roughMass = roughMass;
        return this;
    }

    public Integer getSalvageValue() {
        return salvageValue;
    }

    public ScenarioAnalysis setSalvageValue(Integer salvageValue) {
        this.salvageValue = salvageValue;
        return this;
    }

    public Integer getSalvageValueFactor() {
        return salvageValueFactor;
    }

    public ScenarioAnalysis setSalvageValueFactor(Integer salvageValueFactor) {
        this.salvageValueFactor = salvageValueFactor;
        return this;
    }

    public Integer getScrapMass() {
        return scrapMass;
    }

    public ScenarioAnalysis setScrapMass(Integer scrapMass) {
        this.scrapMass = scrapMass;
        return this;
    }

    public Integer getScrapPartCredit() {
        return scrapPartCredit;
    }

    public ScenarioAnalysis setScrapPartCredit(Integer scrapPartCredit) {
        this.scrapPartCredit = scrapPartCredit;
        return this;
    }

    public Integer getSetupCostPerCore() {
        return setupCostPerCore;
    }

    public ScenarioAnalysis setSetupCostPerCore(Integer setupCostPerCore) {
        this.setupCostPerCore = setupCostPerCore;
        return this;
    }

    public Integer getSetupCostPerPart() {
        return setupCostPerPart;
    }

    public ScenarioAnalysis setSetupCostPerPart(Integer setupCostPerPart) {
        this.setupCostPerPart = setupCostPerPart;
        return this;
    }

    public Integer getSgaCost() {
        return sgaCost;
    }

    public ScenarioAnalysis setSgaCost(Integer sgaCost) {
        this.sgaCost = sgaCost;
        return this;
    }

    public Integer getSgaPercent() {
        return sgaPercent;
    }

    public ScenarioAnalysis setSgaPercent(Integer sgaPercent) {
        this.sgaPercent = sgaPercent;
        return this;
    }

    public Integer getShiftsPerDay() {
        return shiftsPerDay;
    }

    public ScenarioAnalysis setShiftsPerDay(Integer shiftsPerDay) {
        this.shiftsPerDay = shiftsPerDay;
        return this;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public ScenarioAnalysis setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
        return this;
    }

    public Integer getStockPropertyHeight() {
        return stockPropertyHeight;
    }

    public ScenarioAnalysis setStockPropertyHeight(Integer stockPropertyHeight) {
        this.stockPropertyHeight = stockPropertyHeight;
        return this;
    }

    public Integer getStockPropertyInsideDia() {
        return stockPropertyInsideDia;
    }

    public ScenarioAnalysis setStockPropertyInsideDia(Integer stockPropertyInsideDia) {
        this.stockPropertyInsideDia = stockPropertyInsideDia;
        return this;
    }

    public Integer getStockPropertyLength() {
        return stockPropertyLength;
    }

    public ScenarioAnalysis setStockPropertyLength(Integer stockPropertyLength) {
        this.stockPropertyLength = stockPropertyLength;
        return this;
    }

    public Integer getStockPropertyOutsideDia() {
        return stockPropertyOutsideDia;
    }

    public ScenarioAnalysis setStockPropertyOutsideDia(Integer stockPropertyOutsideDia) {
        this.stockPropertyOutsideDia = stockPropertyOutsideDia;
        return this;
    }

    public Integer getStockPropertyThickness() {
        return stockPropertyThickness;
    }

    public ScenarioAnalysis setStockPropertyThickness(Integer stockPropertyThickness) {
        this.stockPropertyThickness = stockPropertyThickness;
        return this;
    }

    public Integer getStockPropertyWallThickness() {
        return stockPropertyWallThickness;
    }

    public ScenarioAnalysis setStockPropertyWallThickness(Integer stockPropertyWallThickness) {
        this.stockPropertyWallThickness = stockPropertyWallThickness;
        return this;
    }

    public Integer getStockPropertyWidth() {
        return stockPropertyWidth;
    }

    public ScenarioAnalysis setStockPropertyWidth(Integer stockPropertyWidth) {
        this.stockPropertyWidth = stockPropertyWidth;
        return this;
    }

    public Integer getStripNestingPitch() {
        return stripNestingPitch;
    }

    public ScenarioAnalysis setStripNestingPitch(Integer stripNestingPitch) {
        this.stripNestingPitch = stripNestingPitch;
        return this;
    }

    public Integer getSuppliesCost() {
        return suppliesCost;
    }

    public ScenarioAnalysis setSuppliesCost(Integer suppliesCost) {
        this.suppliesCost = suppliesCost;
        return this;
    }

    public Integer getSupportAllocation() {
        return supportAllocation;
    }

    public ScenarioAnalysis setSupportAllocation(Integer supportAllocation) {
        this.supportAllocation = supportAllocation;
        return this;
    }

    public Integer getSupportServicesCost() {
        return supportServicesCost;
    }

    public ScenarioAnalysis setSupportServicesCost(Integer supportServicesCost) {
        this.supportServicesCost = supportServicesCost;
        return this;
    }

    public Integer getToolCribDepartmentCost() {
        return toolCribDepartmentCost;
    }

    public ScenarioAnalysis setToolCribDepartmentCost(Integer toolCribDepartmentCost) {
        this.toolCribDepartmentCost = toolCribDepartmentCost;
        return this;
    }

    public Integer getToolCribSupportAllocation() {
        return toolCribSupportAllocation;
    }

    public ScenarioAnalysis setToolCribSupportAllocation(Integer toolCribSupportAllocation) {
        this.toolCribSupportAllocation = toolCribSupportAllocation;
        return this;
    }

    public Integer getToolCribSupportRate() {
        return toolCribSupportRate;
    }

    public ScenarioAnalysis setToolCribSupportRate(Integer toolCribSupportRate) {
        this.toolCribSupportRate = toolCribSupportRate;
        return this;
    }

    public Integer getToolingCostPerPart() {
        return toolingCostPerPart;
    }

    public ScenarioAnalysis setToolingCostPerPart(Integer toolingCostPerPart) {
        this.toolingCostPerPart = toolingCostPerPart;
        return this;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public ScenarioAnalysis setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public Integer getTotalMachineCost() {
        return totalMachineCost;
    }

    public ScenarioAnalysis setTotalMachineCost(Integer totalMachineCost) {
        this.totalMachineCost = totalMachineCost;
        return this;
    }

    public Integer getTotalProductionVolume() {
        return totalProductionVolume;
    }

    public ScenarioAnalysis setTotalProductionVolume(Integer totalProductionVolume) {
        this.totalProductionVolume = totalProductionVolume;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ScenarioAnalysis setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ScenarioAnalysis setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public ScenarioAnalysis setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public Boolean getUseComputedOverheadRate() {
        return useComputedOverheadRate;
    }

    public ScenarioAnalysis setUseComputedOverheadRate(Boolean useComputedOverheadRate) {
        this.useComputedOverheadRate = useComputedOverheadRate;
        return this;
    }

    public Boolean getUseIndirectOverheadPercentage() {
        return useIndirectOverheadPercentage;
    }

    public ScenarioAnalysis setUseIndirectOverheadPercentage(Boolean useIndirectOverheadPercentage) {
        this.useIndirectOverheadPercentage = useIndirectOverheadPercentage;
        return this;
    }

    public Integer getUtilitiesCost() {
        return utilitiesCost;
    }

    public ScenarioAnalysis setUtilitiesCost(Integer utilitiesCost) {
        this.utilitiesCost = utilitiesCost;
        return this;
    }

    public Integer getUtilization() {
        return utilization;
    }

    public ScenarioAnalysis setUtilization(Integer utilization) {
        this.utilization = utilization;
        return this;
    }

    public Integer getUtilizationWithAddendum() {
        return utilizationWithAddendum;
    }

    public ScenarioAnalysis setUtilizationWithAddendum(Integer utilizationWithAddendum) {
        this.utilizationWithAddendum = utilizationWithAddendum;
        return this;
    }

    public Integer getUtilizationWithoutAddendum() {
        return utilizationWithoutAddendum;
    }

    public ScenarioAnalysis setUtilizationWithoutAddendum(Integer utilizationWithoutAddendum) {
        this.utilizationWithoutAddendum = utilizationWithoutAddendum;
        return this;
    }

    public String getVirtualMaterialStockName() {
        return virtualMaterialStockName;
    }

    public ScenarioAnalysis setVirtualMaterialStockName(String virtualMaterialStockName) {
        this.virtualMaterialStockName = virtualMaterialStockName;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public ScenarioAnalysis setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getWorkCenterCapacity() {
        return workCenterCapacity;
    }

    public ScenarioAnalysis setWorkCenterCapacity(Integer workCenterCapacity) {
        this.workCenterCapacity = workCenterCapacity;
        return this;
    }

    public Integer getWorkCenterFootprint() {
        return workCenterFootprint;
    }

    public ScenarioAnalysis setWorkCenterFootprint(Integer workCenterFootprint) {
        this.workCenterFootprint = workCenterFootprint;
        return this;
    }
}