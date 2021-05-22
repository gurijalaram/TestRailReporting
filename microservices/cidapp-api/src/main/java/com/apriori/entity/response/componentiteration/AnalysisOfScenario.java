package com.apriori.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalysisOfScenario {
    private String identity;
    private Double additionalAmortizedInvestment;
    private Double additionalDirectCosts;
    private Double amortizedInvestment;
    private Double annualCost;
    private Integer annualVolume;
    private Double batchCost;
    private Double batchSetupTime;
    private Integer batchSize;
    private Double cadSerLength;
    private Double cadSerWidth;
    private Double capitalInvestment;
    private Double cycleTime;
    private Integer dfmRisk;
    private Double directOverheadCost;
    private Integer dtcMessagesCount;
    private Double elapsedTime;
    private Double expendableToolingCostPerPart;
    private Double extraCosts;
    private Integer failedGcdsCount;
    private Integer failuresWarningsCount;
    private Double finalYield;
    private Double finishMass;
    private Double fixtureCost;
    private Double fixtureCostPerPart;
    private Double fullyBurdenedCost;
    private Integer gcdWithTolerancesCount;
    private Double goodPartYield;
    private Double hardToolingCost;
    private Double height;
    private Double laborCost;
    private Double laborTime;
    private String lastCosted;
    private Double length;
    private Double lifetimeCost;
    private Double logisticsCost;
    private Double margin;
    private Double marginPercent;
    private Double materialCost;
    private String materialName;
    private Double materialOverheadCost;
    private String materialStockFormName;
    private String materialStockName;
    private Double materialUnitCost;
    private Integer notSupportedGcdsCount;
    private Double numberOfParts;
    private Double numOperators;
    private Double numPartsPerSheet;
    private Double numScrapParts;
    private Double partsPerHour;
    private Double periodOverhead;
    private Double pieceAndPeriod;
    private Double pieceCost;
    private String processRoutingName;
    private Double programmingCost;
    private Double programmingCostPerPart;
    private Double roughMass;
    private Double scrapMass;
    private Double setupCostPerPart;
    private Double sgaCost;
    private Double stockPropertyLength;
    private Double stockPropertyThickness;
    private Double stockPropertyWidth;
    private Double stripNestingPitch;
    private Double toolingCostPerPart;
    private Double totalCost;
    private Double totalProductionVolume;
    private Double otherDirectCosts;
    private Double utilization;
    private Double utilizationWithAddendum;
    private Double utilizationWithoutAddendum;
    private String virtualMaterialStockName;
    private Double width;

    public String getIdentity() {
        return identity;
    }

    public AnalysisOfScenario setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Double getAdditionalAmortizedInvestment() {
        return additionalAmortizedInvestment;
    }

    public AnalysisOfScenario setAdditionalAmortizedInvestment(Double additionalAmortizedInvestment) {
        this.additionalAmortizedInvestment = additionalAmortizedInvestment;
        return this;
    }

    public Double getAdditionalDirectCosts() {
        return additionalDirectCosts;
    }

    public AnalysisOfScenario setAdditionalDirectCosts(Double additionalDirectCosts) {
        this.additionalDirectCosts = additionalDirectCosts;
        return this;
    }

    public Double getAmortizedInvestment() {
        return amortizedInvestment;
    }

    public AnalysisOfScenario setAmortizedInvestment(Double amortizedInvestment) {
        this.amortizedInvestment = amortizedInvestment;
        return this;
    }

    public Double getAnnualCost() {
        return annualCost;
    }

    public AnalysisOfScenario setAnnualCost(Double annualCost) {
        this.annualCost = annualCost;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public AnalysisOfScenario setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Double getBatchCost() {
        return batchCost;
    }

    public AnalysisOfScenario setBatchCost(Double batchCost) {
        this.batchCost = batchCost;
        return this;
    }

    public Double getBatchSetupTime() {
        return batchSetupTime;
    }

    public AnalysisOfScenario setBatchSetupTime(Double batchSetupTime) {
        this.batchSetupTime = batchSetupTime;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public AnalysisOfScenario setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Double getCadSerLength() {
        return cadSerLength;
    }

    public AnalysisOfScenario setCadSerLength(Double cadSerLength) {
        this.cadSerLength = cadSerLength;
        return this;
    }

    public Double getCadSerWidth() {
        return cadSerWidth;
    }

    public AnalysisOfScenario setCadSerWidth(Double cadSerWidth) {
        this.cadSerWidth = cadSerWidth;
        return this;
    }

    public Double getCapitalInvestment() {
        return capitalInvestment;
    }

    public AnalysisOfScenario setCapitalInvestment(Double capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
        return this;
    }

    public Double getCycleTime() {
        return cycleTime;
    }

    public AnalysisOfScenario setCycleTime(Double cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public Integer getDfmRisk() {
        return dfmRisk;
    }

    public AnalysisOfScenario setDfmRisk(Integer dfmRisk) {
        this.dfmRisk = dfmRisk;
        return this;
    }

    public Double getDirectOverheadCost() {
        return directOverheadCost;
    }

    public AnalysisOfScenario setDirectOverheadCost(Double directOverheadCost) {
        this.directOverheadCost = directOverheadCost;
        return this;
    }

    public Integer getDtcMessagesCount() {
        return dtcMessagesCount;
    }

    public AnalysisOfScenario setDtcMessagesCount(Integer dtcMessagesCount) {
        this.dtcMessagesCount = dtcMessagesCount;
        return this;
    }

    public Double getElapsedTime() {
        return elapsedTime;
    }

    public AnalysisOfScenario setElapsedTime(Double elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public Double getExpendableToolingCostPerPart() {
        return expendableToolingCostPerPart;
    }

    public AnalysisOfScenario setExpendableToolingCostPerPart(Double expendableToolingCostPerPart) {
        this.expendableToolingCostPerPart = expendableToolingCostPerPart;
        return this;
    }

    public Double getExtraCosts() {
        return extraCosts;
    }

    public AnalysisOfScenario setExtraCosts(Double extraCosts) {
        this.extraCosts = extraCosts;
        return this;
    }

    public Integer getFailedGcdsCount() {
        return failedGcdsCount;
    }

    public AnalysisOfScenario setFailedGcdsCount(Integer failedGcdsCount) {
        this.failedGcdsCount = failedGcdsCount;
        return this;
    }

    public Integer getFailuresWarningsCount() {
        return failuresWarningsCount;
    }

    public AnalysisOfScenario setFailuresWarningsCount(Integer failuresWarningsCount) {
        this.failuresWarningsCount = failuresWarningsCount;
        return this;
    }

    public Double getFinalYield() {
        return finalYield;
    }

    public AnalysisOfScenario setFinalYield(Double finalYield) {
        this.finalYield = finalYield;
        return this;
    }

    public Double getFinishMass() {
        return finishMass;
    }

    public AnalysisOfScenario setFinishMass(Double finishMass) {
        this.finishMass = finishMass;
        return this;
    }

    public Double getFixtureCost() {
        return fixtureCost;
    }

    public AnalysisOfScenario setFixtureCost(Double fixtureCost) {
        this.fixtureCost = fixtureCost;
        return this;
    }

    public Double getFixtureCostPerPart() {
        return fixtureCostPerPart;
    }

    public AnalysisOfScenario setFixtureCostPerPart(Double fixtureCostPerPart) {
        this.fixtureCostPerPart = fixtureCostPerPart;
        return this;
    }

    public Double getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public AnalysisOfScenario setFullyBurdenedCost(Double fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    public Integer getGcdWithTolerancesCount() {
        return gcdWithTolerancesCount;
    }

    public AnalysisOfScenario setGcdWithTolerancesCount(Integer gcdWithTolerancesCount) {
        this.gcdWithTolerancesCount = gcdWithTolerancesCount;
        return this;
    }

    public Double getGoodPartYield() {
        return goodPartYield;
    }

    public AnalysisOfScenario setGoodPartYield(Double goodPartYield) {
        this.goodPartYield = goodPartYield;
        return this;
    }

    public Double getHardToolingCost() {
        return hardToolingCost;
    }

    public AnalysisOfScenario setHardToolingCost(Double hardToolingCost) {
        this.hardToolingCost = hardToolingCost;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public AnalysisOfScenario setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getLaborCost() {
        return laborCost;
    }

    public AnalysisOfScenario setLaborCost(Double laborCost) {
        this.laborCost = laborCost;
        return this;
    }

    public Double getLaborTime() {
        return laborTime;
    }

    public AnalysisOfScenario setLaborTime(Double laborTime) {
        this.laborTime = laborTime;
        return this;
    }

    public String getLastCosted() {
        return lastCosted;
    }

    public AnalysisOfScenario setLastCosted(String lastCosted) {
        this.lastCosted = lastCosted;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public AnalysisOfScenario setLength(Double length) {
        this.length = length;
        return this;
    }

    public Double getLifetimeCost() {
        return lifetimeCost;
    }

    public AnalysisOfScenario setLifetimeCost(Double lifetimeCost) {
        this.lifetimeCost = lifetimeCost;
        return this;
    }

    public Double getLogisticsCost() {
        return logisticsCost;
    }

    public AnalysisOfScenario setLogisticsCost(Double logisticsCost) {
        this.logisticsCost = logisticsCost;
        return this;
    }

    public Double getMargin() {
        return margin;
    }

    public AnalysisOfScenario setMargin(Double margin) {
        this.margin = margin;
        return this;
    }

    public Double getMarginPercent() {
        return marginPercent;
    }

    public AnalysisOfScenario setMarginPercent(Double marginPercent) {
        this.marginPercent = marginPercent;
        return this;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public AnalysisOfScenario setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public AnalysisOfScenario setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public Double getMaterialOverheadCost() {
        return materialOverheadCost;
    }

    public AnalysisOfScenario setMaterialOverheadCost(Double materialOverheadCost) {
        this.materialOverheadCost = materialOverheadCost;
        return this;
    }

    public String getMaterialStockFormName() {
        return materialStockFormName;
    }

    public AnalysisOfScenario setMaterialStockFormName(String materialStockFormName) {
        this.materialStockFormName = materialStockFormName;
        return this;
    }

    public String getMaterialStockName() {
        return materialStockName;
    }

    public AnalysisOfScenario setMaterialStockName(String materialStockName) {
        this.materialStockName = materialStockName;
        return this;
    }

    public Double getMaterialUnitCost() {
        return materialUnitCost;
    }

    public AnalysisOfScenario setMaterialUnitCost(Double materialUnitCost) {
        this.materialUnitCost = materialUnitCost;
        return this;
    }

    public Integer getNotSupportedGcdsCount() {
        return notSupportedGcdsCount;
    }

    public AnalysisOfScenario setNotSupportedGcdsCount(Integer notSupportedGcdsCount) {
        this.notSupportedGcdsCount = notSupportedGcdsCount;
        return this;
    }

    public Double getNumberOfParts() {
        return numberOfParts;
    }

    public AnalysisOfScenario setNumberOfParts(Double numberOfParts) {
        this.numberOfParts = numberOfParts;
        return this;
    }

    public Double getNumOperators() {
        return numOperators;
    }

    public AnalysisOfScenario setNumOperators(Double numOperators) {
        this.numOperators = numOperators;
        return this;
    }

    public Double getNumPartsPerSheet() {
        return numPartsPerSheet;
    }

    public AnalysisOfScenario setNumPartsPerSheet(Double numPartsPerSheet) {
        this.numPartsPerSheet = numPartsPerSheet;
        return this;
    }

    public Double getNumScrapParts() {
        return numScrapParts;
    }

    public AnalysisOfScenario setNumScrapParts(Double numScrapParts) {
        this.numScrapParts = numScrapParts;
        return this;
    }

    public Double getPartsPerHour() {
        return partsPerHour;
    }

    public AnalysisOfScenario setPartsPerHour(Double partsPerHour) {
        this.partsPerHour = partsPerHour;
        return this;
    }

    public Double getPeriodOverhead() {
        return periodOverhead;
    }

    public AnalysisOfScenario setPeriodOverhead(Double periodOverhead) {
        this.periodOverhead = periodOverhead;
        return this;
    }

    public Double getPieceAndPeriod() {
        return pieceAndPeriod;
    }

    public AnalysisOfScenario setPieceAndPeriod(Double pieceAndPeriod) {
        this.pieceAndPeriod = pieceAndPeriod;
        return this;
    }

    public Double getPieceCost() {
        return pieceCost;
    }

    public AnalysisOfScenario setPieceCost(Double pieceCost) {
        this.pieceCost = pieceCost;
        return this;
    }

    public String getProcessRoutingName() {
        return processRoutingName;
    }

    public AnalysisOfScenario setProcessRoutingName(String processRoutingName) {
        this.processRoutingName = processRoutingName;
        return this;
    }

    public Double getProgrammingCost() {
        return programmingCost;
    }

    public AnalysisOfScenario setProgrammingCost(Double programmingCost) {
        this.programmingCost = programmingCost;
        return this;
    }

    public Double getProgrammingCostPerPart() {
        return programmingCostPerPart;
    }

    public AnalysisOfScenario setProgrammingCostPerPart(Double programmingCostPerPart) {
        this.programmingCostPerPart = programmingCostPerPart;
        return this;
    }

    public Double getRoughMass() {
        return roughMass;
    }

    public AnalysisOfScenario setRoughMass(Double roughMass) {
        this.roughMass = roughMass;
        return this;
    }

    public Double getScrapMass() {
        return scrapMass;
    }

    public AnalysisOfScenario setScrapMass(Double scrapMass) {
        this.scrapMass = scrapMass;
        return this;
    }

    public Double getSetupCostPerPart() {
        return setupCostPerPart;
    }

    public AnalysisOfScenario setSetupCostPerPart(Double setupCostPerPart) {
        this.setupCostPerPart = setupCostPerPart;
        return this;
    }

    public Double getSgaCost() {
        return sgaCost;
    }

    public AnalysisOfScenario setSgaCost(Double sgaCost) {
        this.sgaCost = sgaCost;
        return this;
    }

    public Double getStockPropertyLength() {
        return stockPropertyLength;
    }

    public AnalysisOfScenario setStockPropertyLength(Double stockPropertyLength) {
        this.stockPropertyLength = stockPropertyLength;
        return this;
    }

    public Double getStockPropertyThickness() {
        return stockPropertyThickness;
    }

    public AnalysisOfScenario setStockPropertyThickness(Double stockPropertyThickness) {
        this.stockPropertyThickness = stockPropertyThickness;
        return this;
    }

    public Double getStockPropertyWidth() {
        return stockPropertyWidth;
    }

    public AnalysisOfScenario setStockPropertyWidth(Double stockPropertyWidth) {
        this.stockPropertyWidth = stockPropertyWidth;
        return this;
    }

    public Double getStripNestingPitch() {
        return stripNestingPitch;
    }

    public AnalysisOfScenario setStripNestingPitch(Double stripNestingPitch) {
        this.stripNestingPitch = stripNestingPitch;
        return this;
    }

    public Double getToolingCostPerPart() {
        return toolingCostPerPart;
    }

    public AnalysisOfScenario setToolingCostPerPart(Double toolingCostPerPart) {
        this.toolingCostPerPart = toolingCostPerPart;
        return this;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public AnalysisOfScenario setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public Double getTotalProductionVolume() {
        return totalProductionVolume;
    }

    public AnalysisOfScenario setTotalProductionVolume(Double totalProductionVolume) {
        this.totalProductionVolume = totalProductionVolume;
        return this;
    }

    public Double getOtherDirectCosts() {
        return otherDirectCosts;
    }

    public AnalysisOfScenario setOtherDirectCosts(Double otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
        return this;
    }

    public Double getUtilization() {
        return utilization;
    }

    public AnalysisOfScenario setUtilization(Double utilization) {
        this.utilization = utilization;
        return this;
    }

    public Double getUtilizationWithAddendum() {
        return utilizationWithAddendum;
    }

    public AnalysisOfScenario setUtilizationWithAddendum(Double utilizationWithAddendum) {
        this.utilizationWithAddendum = utilizationWithAddendum;
        return this;
    }

    public Double getUtilizationWithoutAddendum() {
        return utilizationWithoutAddendum;
    }

    public AnalysisOfScenario setUtilizationWithoutAddendum(Double utilizationWithoutAddendum) {
        this.utilizationWithoutAddendum = utilizationWithoutAddendum;
        return this;
    }

    public String getVirtualMaterialStockName() {
        return virtualMaterialStockName;
    }

    public AnalysisOfScenario setVirtualMaterialStockName(String virtualMaterialStockName) {
        this.virtualMaterialStockName = virtualMaterialStockName;
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public AnalysisOfScenario setWidth(Double width) {
        this.width = width;
        return this;
    }
}
