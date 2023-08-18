package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AnalysisOfScenario {
    private String identity;
    private int additionalAmortizedInvestment;
    private int additionalDirectCosts;
    private double amortizedInvestment;
    private double annualCost;
    private int annualVolume;
    private double batchCost;
    private int batchSetupTime;
    private int batchSize;
    private int cadSerLength;
    private int cadSerWidth;
    private double capitalInvestment;
    private double cycleTime;
    private String dfmRisk;
    private int dfmScore;
    private String digitalFactoryDefaultMaterialName;
    private double directOverheadCost;
    private int dtcMessagesCount;
    private double elapsedTime;
    private int expendableToolingCostPerPart;
    private int extraCosts;
    private int failedGcdsCount;
    private int failuresWarningsCount;
    private int finalYield;
    private double finishMass;
    private int fixtureCost;
    private int fixtureCostPerPart;
    private double fullyBurdenedCost;
    private int gcdWithTolerancesCount;
    private int goodPartYield;
    private double hardToolingCost;
    private int height;
    private double laborCost;
    private double laborTime;
    private String lastCosted;
    private double length;
    private double lifetimeCost;
    private int logisticsCost;
    private String manualMaterialName;
    private int margin;
    private int marginPercent;
    private double materialCost;
    private String materialMode;
    private String materialName;
    private double materialOverheadCost;
    private String  materialStockFormName;
    private String  materialStockName;
    private double materialUnitCost;
    private String materialUtilizationMode;
    private int notSupportedGcdsCount;
    private int numberOfParts;
    private int numOperators;
    private int numPartsPerSheet;
    private int numScrapParts;
    private double partsPerHour;
    private double periodOverhead;
    private double pieceAndPeriod;
    private double pieceCost;
    private String processRoutingName;
    private int productionLife;
    private int programmingCost;
    private int programmingCostPerPart;
    private double roughMass;
    private double scrapMass;
    private double setupCostPerPart;
    private double sgaCost;
    private double stockPropertyHeight;
    private double stockPropertyInsideDia;
    private double stockPropertyLength;
    private double stockPropertyOutsideDia;
    private double stockPropertyWallThickness;
    private double stockPropertyThickness;
    private double stockPropertyWidth;
    private int stripNestingPitch;
    private double toolingCostPerPart;
    private double totalCost;
    private int totalProductionVolume;
    private double otherDirectCosts;
    private double utilization;
    private int utilizationWithAddendum;
    private int utilizationWithoutAddendum;
    private String virtualMaterialStockName;
    private double width;
}
