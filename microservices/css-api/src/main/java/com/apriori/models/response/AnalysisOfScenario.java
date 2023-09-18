package com.apriori.models.response;

import com.apriori.enums.DfmRisk;

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
    private Integer additionalAmortizedInvestment;
    private Integer additionalDirectCosts;
    private Double amortizedInvestment;
    private Double annualCost;
    private Double annualManufacturingCarbon;
    private Integer annualVolume;
    private Double batchCost;
    private Integer batchSetupTime;
    private Integer batchSize;
    private Integer cadSerLength;
    private Integer cadSerWidth;
    private Double capitalInvestment;
    private Double cycleTime;
    private DfmRisk dfmRisk;
    private Integer dfmScore;
    private String digitalFactoryDefaultMaterialName;
    private Double directOverheadCost;
    private Integer dtcMessagesCount;
    private Double elapsedTime;
    private Integer expendableToolingCostPerPart;
    private Integer extraCosts;
    private Integer failedGcdsCount;
    private Integer failuresWarningsCount;
    private Integer finalYield;
    private Double finishMass;
    private Integer fixtureCost;
    private Integer fixtureCostPerPart;
    private Double fullyBurdenedCost;
    private Integer gcdWithTolerancesCount;
    private Integer goodPartYield;
    private Double hardToolingCost;
    private Integer height;
    private Double laborCost;
    private Double laborTime;
    private String lastCosted;
    private Double length;
    private Double lifetimeCost;
    private Integer logisticsCost;
    private String manualMaterialName;
    private Integer margin;
    private Integer marginPercent;
    private Double materialCost;
    private String materialMode;
    private String materialName;
    private Double materialOverheadCost;
    private String  materialStockFormName;
    private String  materialStockName;
    private Double materialUnitCost;
    private String materialUtilizationMode;
    private Integer notSupportedGcdsCount;
    private Integer numberOfParts;
    private Integer numOperators;
    private Integer numPartsPerSheet;
    private Integer numScrapParts;
    private Double partsPerHour;
    private Double periodOverhead;
    private Double pieceAndPeriod;
    private Double pieceCost;
    private String processRoutingName;
    private Integer productionLife;
    private Integer programmingCost;
    private Integer programmingCostPerPart;
    private Double roughMass;
    private Double scrapMass;
    private Double setupCostPerPart;
    private Double sgaCost;
    private Double stockPropertyHeight;
    private Double stockPropertyInsideDia;
    private Double stockPropertyLength;
    private Double stockPropertyOutsideDia;
    private Double stockPropertyWallThickness;
    private Double stockPropertyThickness;
    private Double stockPropertyWidth;
    private Integer stripNestingPitch;
    private Double toolingCostPerPart;
    private Double totalCost;
    private Integer totalProductionVolume;
    private Double otherDirectCosts;
    private Double utilization;
    private Integer utilizationWithAddendum;
    private Integer utilizationWithoutAddendum;
    private String virtualMaterialStockName;
    private Double width;
}
