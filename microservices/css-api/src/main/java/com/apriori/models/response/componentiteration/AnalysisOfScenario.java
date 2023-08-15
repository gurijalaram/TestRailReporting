package com.apriori.models.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
    private String dfmRisk;
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
    private Double dfmScore;
    private String cadMaterialName;
    private String digitalFactoryDefaultMaterialName;
    private String manualMaterialName;
    private String materialMode;
    private String materialUtilizationMode;
    private String productionLife;
    private Double stockPropertyHeight;
    private Double stockPropertyInsideDia;
    private Double stockPropertyOutsideDia;
    private Double stockPropertyWallThickness;
}
