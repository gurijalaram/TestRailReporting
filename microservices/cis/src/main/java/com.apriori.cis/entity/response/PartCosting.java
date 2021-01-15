package com.apriori.cis.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "PartCostingSchema.json")
public class PartCosting {
    private PartCosting response;
    private String partIdentity;
    private String url;

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
    private String costingStatus;
    private String currencyCode;
    private Double cycleTime;
    private String description;
    private String dfmRisk;
    private Integer dfmScore;
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

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime lastCosted;

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
    private Double otherDirectCosts;
    private Double partsPerHour;
    private Double periodOverhead;
    private Double pieceAndPeriod;
    private Double pieceCost;
    private String processGroupName;
    private String processRoutingName;
    private Double productionLife;
    private Double programmingCost;
    private Double programmingCostPerPart;
    private Double roughMass;
    private String scenarioName;
    private Double scrapMass;
    private Double setupCostPerPart;
    private Double sgaCost;
    private Double stockPropertyHeight;
    private Double stockPropertyInsideDia;
    private Double stockPropertyLength;
    private Double stockPropertyOutsideDia;
    private Double stockPropertyThickness;
    private Double stockPropertyWallThickness;
    private Double stockPropertyWidth;
    private Double stripNestingPitch;
    private Double toolingCostPerPart;
    private Double totalCost;
    private Double totalProductionVolume;
    private Object userDefinedAttributes;
    private Double utilization;
    private Double utilizationWithAddendum;
    private Double utilizationWithoutAddendum;
    private String virtualMaterialStockName;
    private String vpeName;
    private Double width;

    public LocalDateTime setLastCosted() {
        return this.lastCosted;
    }

    public PartCosting setCreatedAt(LocalDateTime lastCosted) {
        this.lastCosted = lastCosted;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public PartCosting setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public Integer getDfmScore() {
        return dfmScore;
    }

    public PartCosting setDfmScore(Integer dfmScore) {
        this.dfmScore = dfmScore;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PartCosting setDescription(String description) {
        this.description = description;
        return this;
    }

    public Object getUserDefinedAttributes() {
        return userDefinedAttributes;
    }

    public PartCosting setUserDefinedAttributes(Object userDefinedAttributes) {
        this.userDefinedAttributes = userDefinedAttributes;
        return this;
    }

    public String getVirtualMaterialStockName() {
        return virtualMaterialStockName;
    }

    public PartCosting setVirtualMaterialStockName(String virtualMaterialStockName) {
        this.virtualMaterialStockName = virtualMaterialStockName;
        return this;
    }

    public Double getStockPropertyInsideDia() {
        return stockPropertyInsideDia;
    }

    public PartCosting setStockPropertyInsideDia(Double stockPropertyInsideDia) {
        this.stockPropertyInsideDia = stockPropertyInsideDia;
        return this;
    }

    public Double getStockPropertyOutsideDia() {
        return stockPropertyOutsideDia;
    }

    public PartCosting setStockPropertyOutsideDia(Double stockPropertyOutsideDia) {
        this.stockPropertyOutsideDia = stockPropertyOutsideDia;
        return this;
    }

    public Double getStockPropertyWallThickness() {
        return stockPropertyWallThickness;
    }

    public PartCosting setStockPropertyWallThickness(Double stockPropertyWallThickness) {
        this.stockPropertyWallThickness = stockPropertyWallThickness;
        return this;
    }

    public Double getStockPropertyHeight() {
        return stockPropertyHeight;
    }

    public void setStockPropertyHeight(Double stockPropertyHeight) {
        this.stockPropertyHeight = stockPropertyHeight;
    }

    public Double getAdditionalDirectCosts() {
        return additionalDirectCosts;
    }

    public PartCosting setAdditionalDirectCosts(Double additionalDirectCosts) {
        this.additionalDirectCosts = additionalDirectCosts;
        return this;
    }

    public Double getNumberOfParts() {
        return numberOfParts;
    }

    public PartCosting setNumberOfParts(Double numberOfParts) {
        this.numberOfParts = numberOfParts;
        return this;
    }

    public Double getNumOperators() {
        return numOperators;
    }

    public PartCosting setNumOperators(Double numOperators) {
        this.numOperators = numOperators;
        return this;
    }

    public Double getNumPartsPerSheet() {
        return numPartsPerSheet;
    }

    public PartCosting setNumPartsPerSheet(Double numPartsPerSheet) {
        this.numPartsPerSheet = numPartsPerSheet;
        return this;
    }

    public Double getNumScrapParts() {
        return numScrapParts;
    }

    public PartCosting setNumScrapParts(Double numScrapParts) {
        this.numScrapParts = numScrapParts;
        return this;
    }

    public Double getPartsPerHour() {
        return partsPerHour;
    }

    public PartCosting setPartsPerHour(Double partsPerHour) {
        this.partsPerHour = partsPerHour;
        return this;
    }

    public Double getPeriodOverhead() {
        return periodOverhead;
    }

    public PartCosting setPeriodOverhead(Double periodOverhead) {
        this.periodOverhead = periodOverhead;
        return this;
    }

    public Double getPieceAndPeriod() {
        return pieceAndPeriod;
    }

    public PartCosting setPieceAndPeriod(Double pieceAndPeriod) {
        this.pieceAndPeriod = pieceAndPeriod;
        return this;
    }

    public Double getPieceCost() {
        return pieceCost;
    }

    public PartCosting setPieceCost(Double pieceCost) {
        this.pieceCost = pieceCost;
        return this;
    }

    public Double getProductionLife() {
        return productionLife;
    }

    public PartCosting setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public Double getProgrammingCost() {
        return programmingCost;
    }

    public PartCosting setProgrammingCost(Double programmingCost) {
        this.programmingCost = programmingCost;
        return this;
    }

    public Double getProgrammingCostPerPart() {
        return programmingCostPerPart;
    }

    public PartCosting setProgrammingCostPerPart(Double programmingCostPerPart) {
        this.programmingCostPerPart = programmingCostPerPart;
        return this;
    }

    public Double getRoughMass() {
        return roughMass;
    }

    public PartCosting setRoughMass(Double roughMass) {
        this.roughMass = roughMass;
        return this;
    }

    public Double getScrapMass() {
        return scrapMass;
    }

    public PartCosting setScrapMass(Double scrapMass) {
        this.scrapMass = scrapMass;
        return this;
    }

    public Double getSetupCostPerPart() {
        return setupCostPerPart;
    }

    public PartCosting setSetupCostPerPart(Double setupCostPerPart) {
        this.setupCostPerPart = setupCostPerPart;
        return this;
    }

    public Double getSgaCost() {
        return sgaCost;
    }

    public PartCosting setSgaCost(Double sgaCost) {
        this.sgaCost = sgaCost;
        return this;
    }

    public Double getStockPropertyLength() {
        return stockPropertyLength;
    }

    public PartCosting setStockPropertyLength(Double stockPropertyLength) {
        this.stockPropertyLength = stockPropertyLength;
        return this;
    }

    public Double getStockPropertyThickness() {
        return stockPropertyThickness;
    }

    public PartCosting setStockPropertyThickness(Double stockPropertyThickness) {
        this.stockPropertyThickness = stockPropertyThickness;
        return this;
    }

    public Double getStockPropertyWidth() {
        return stockPropertyWidth;
    }

    public PartCosting setStockPropertyWidth(Double stockPropertyWidth) {
        this.stockPropertyWidth = stockPropertyWidth;
        return this;
    }

    public Double getStripNestingPitch() {
        return stripNestingPitch;
    }

    public PartCosting setStripNestingPitch(Double stripNestingPitch) {
        this.stripNestingPitch = stripNestingPitch;
        return this;
    }

    public Double getToolingCostPerPart() {
        return toolingCostPerPart;
    }

    public PartCosting setToolingCostPerPart(Double toolingCostPerPart) {
        this.toolingCostPerPart = toolingCostPerPart;
        return this;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public PartCosting setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public Double getTotalProductionVolume() {
        return totalProductionVolume;
    }

    public PartCosting setTotalProductionVolume(Double totalProductionVolume) {
        this.totalProductionVolume = totalProductionVolume;
        return this;
    }

    public Double getOtherDirectCosts() {
        return otherDirectCosts;
    }

    public PartCosting setOtherDirectCosts(Double otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
        return this;
    }

    public Double getUtilization() {
        return utilization;
    }

    public PartCosting setUtilization(Double utilization) {
        this.utilization = utilization;
        return this;
    }

    public Double getUtilizationWithAddendum() {
        return utilizationWithAddendum;
    }

    public PartCosting setUtilizationWithAddendum(Double utilizationWithAddendum) {
        this.utilizationWithAddendum = utilizationWithAddendum;
        return this;
    }

    public Double getUtilizationWithoutAddendum() {
        return utilizationWithoutAddendum;
    }

    public PartCosting setUtilizationWithoutAddendum(Double utilizationWithoutAddendum) {
        this.utilizationWithoutAddendum = utilizationWithoutAddendum;
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public PartCosting setWidth(Double width) {
        this.width = width;
        return this;
    }

    public Double getGoodPartYield() {
        return goodPartYield;
    }

    public PartCosting setGoodPartYield(Double goodPartYield) {
        this.goodPartYield = goodPartYield;
        return this;
    }

    public Double getHardToolingCost() {
        return hardToolingCost;
    }

    public PartCosting setHardToolingCost(Double hardToolingCost) {
        this.hardToolingCost = hardToolingCost;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public PartCosting setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getLaborCost() {
        return laborCost;
    }

    public PartCosting setLaborCost(Double laborCost) {
        this.laborCost = laborCost;
        return this;
    }

    public Double getLaborTime() {
        return laborTime;
    }

    public PartCosting setLaborTime(Double laborTime) {
        this.laborTime = laborTime;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public PartCosting setLength(Double length) {
        this.length = length;
        return this;
    }

    public Double getLifetimeCost() {
        return lifetimeCost;
    }

    public PartCosting setLifetimeCost(Double lifetimeCost) {
        this.lifetimeCost = lifetimeCost;
        return this;
    }

    public Double getLogisticsCost() {
        return logisticsCost;
    }

    public PartCosting setLogisticsCost(Double logisticsCost) {
        this.logisticsCost = logisticsCost;
        return this;
    }

    public Double getMargin() {
        return margin;
    }

    public PartCosting setMargin(Double margin) {
        this.margin = margin;
        return this;
    }

    public Double getMarginPercent() {
        return marginPercent;
    }

    public PartCosting setMarginPercent(Double marginPercent) {
        this.marginPercent = marginPercent;
        return this;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public PartCosting setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
        return this;
    }

    public String getMaterialName() {
        return materialName;
    }

    public PartCosting setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public Double getMaterialOverheadCost() {
        return materialOverheadCost;
    }

    public PartCosting setMaterialOverheadCost(Double materialOverheadCost) {
        this.materialOverheadCost = materialOverheadCost;
        return this;
    }

    public String getMaterialStockFormName() {
        return materialStockFormName;
    }

    public PartCosting setMaterialStockFormName(String materialStockFormName) {
        this.materialStockFormName = materialStockFormName;
        return this;
    }

    public String getMaterialStockName() {
        return materialStockName;
    }

    public PartCosting setMaterialStockName(String materialStockName) {
        this.materialStockName = materialStockName;
        return this;
    }

    public Double getMaterialUnitCost() {
        return materialUnitCost;
    }

    public PartCosting setMaterialUnitCost(Double materialUnitCost) {
        this.materialUnitCost = materialUnitCost;
        return this;
    }

    public Double getFinalYield() {
        return finalYield;
    }

    public PartCosting setFinalYield(Double finalYield) {
        this.finalYield = finalYield;
        return this;
    }

    public Double getFinishMass() {
        return finishMass;
    }

    public PartCosting setFinishMass(Double finishMass) {
        this.finishMass = finishMass;
        return this;
    }

    public Double getFixtureCost() {
        return fixtureCost;
    }

    public PartCosting setFixtureCost(Double fixtureCost) {
        this.fixtureCost = fixtureCost;
        return this;
    }

    public Double getFixtureCostPerPart() {
        return fixtureCostPerPart;
    }

    public PartCosting setFixtureCostPerPart(Double fixtureCostPerPart) {
        this.fixtureCostPerPart = fixtureCostPerPart;
        return this;
    }

    public Double getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public PartCosting setFullyBurdenedCost(Double fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    public Double getElapsedTime() {
        return elapsedTime;
    }

    public PartCosting setElapsedTime(Double elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public Double getExpendableToolingCostPerPart() {
        return expendableToolingCostPerPart;
    }

    public PartCosting setExpendableToolingCostPerPart(Double expendableToolingCostPerPart) {
        this.expendableToolingCostPerPart = expendableToolingCostPerPart;
        return this;
    }

    public Double getExtraCosts() {
        return extraCosts;
    }

    public PartCosting setExtraCosts(Double extraCosts) {
        this.extraCosts = extraCosts;
        return this;
    }

    public Double getAmortizedInvestment() {
        return amortizedInvestment;
    }

    public PartCosting setAmortizedInvestment(Double amortizedInvestment) {
        this.amortizedInvestment = amortizedInvestment;
        return this;
    }

    public Double getAnnualCost() {
        return annualCost;
    }

    public PartCosting setAnnualCost(Double annualCost) {
        this.annualCost = annualCost;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public PartCosting setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Double getBatchCost() {
        return batchCost;
    }

    public PartCosting setBatchCost(Double batchCost) {
        this.batchCost = batchCost;
        return this;
    }

    public Double getBatchSetupTime() {
        return batchSetupTime;
    }

    public PartCosting setBatchSetupTime(Double batchSetupTime) {
        this.batchSetupTime = batchSetupTime;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public PartCosting setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Double getCadSerLength() {
        return cadSerLength;
    }

    public PartCosting setCadSerLength(Double cadSerLength) {
        this.cadSerLength = cadSerLength;
        return this;
    }

    public Double getCadSerWidth() {
        return cadSerWidth;
    }

    public PartCosting setCadSerWidth(Double cadSerWidth) {
        this.cadSerWidth = cadSerWidth;
        return this;
    }

    public Double getCapitalInvestment() {
        return capitalInvestment;
    }

    public PartCosting setCapitalInvestment(Double capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
        return this;
    }

    public Double getCycleTime() {
        return cycleTime;
    }

    public PartCosting setCycleTime(Double cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public String getDfmRisk() {
        return dfmRisk;
    }

    public PartCosting setDfmRisk(String dfmRisk) {
        this.dfmRisk = dfmRisk;
        return this;
    }

    public Double getDirectOverheadCost() {
        return directOverheadCost;
    }

    public PartCosting setDirectOverheadCost(Double directOverheadCost) {
        this.directOverheadCost = directOverheadCost;
        return this;
    }

    public Double getAdditionalAmortizedInvestment() {
        return additionalAmortizedInvestment;
    }

    public PartCosting setAdditionalAmortizedInvestment(Double additionalAmortizedInvestment) {
        this.additionalAmortizedInvestment = additionalAmortizedInvestment;
        return this;
    }

    public String getCostingStatus() {
        return costingStatus;
    }

    public PartCosting setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public PartCosting setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public PartCosting setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public String getProcessRoutingName() {
        return processRoutingName;
    }

    public PartCosting setProcessRoutingName(String processRoutingName) {
        this.processRoutingName = processRoutingName;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public PartCosting setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public PartCosting setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPartIdentity() {
        return this.partIdentity;
    }

    public PartCosting setPartIdentity(String partIdentity) {
        this.partIdentity = partIdentity;
        return this;
    }

    public Integer getDtcMessagesCount() {
        return this.dtcMessagesCount;
    }

    public PartCosting setDtcMessagesCount(Integer dtcMessagesCount) {
        this.dtcMessagesCount = dtcMessagesCount;
        return this;
    }

    public Integer getFailedGcdsCount() {
        return this.failedGcdsCount;
    }

    public PartCosting setFailedGcdsCount(Integer failedGcdsCount) {
        this.failedGcdsCount = failedGcdsCount;
        return this;
    }

    public Integer getFailuresWarningsCount() {
        return this.failuresWarningsCount;
    }

    public PartCosting setFailuresWarningsCount(Integer failuresWarningsCount) {
        this.failuresWarningsCount = failuresWarningsCount;
        return this;
    }

    public Integer getGcdWithTolerancesCount() {
        return this.gcdWithTolerancesCount;
    }

    public PartCosting setGcdWithTolerancesCount(Integer gcdWithTolerancesCount) {
        this.gcdWithTolerancesCount = gcdWithTolerancesCount;
        return this;
    }

    public Integer getNotSupportedGcdsCount() {
        return this.notSupportedGcdsCount;
    }

    public PartCosting setNotSupportedGcdsCount(Integer notSupportedGcdsCount) {
        this.notSupportedGcdsCount = notSupportedGcdsCount;
        return this;
    }

    public PartCosting getResponse() {
        return this.response;
    }

    public PartCosting setResponse(PartCosting response) {
        this.response = response;
        return this;
    }
}
