package com.apriori.models.response;

import lombok.Data;

@Data
public class AnalysisOfScenarioAndChildren {
    private String identity;
    private Double additionalAmortizedInvestment;
    private Double additionalDirectCosts;
    private Double amortizedInvestment;
    private Double annualCost;
    private Double batchCost;
    private Double batchSetupTime;
    private Double capitalInvestment;
    private Double cycleTime;
    private Double directOverheadCost;
    private Double expendableToolingCostPerPart;
    private Double extraCosts;
    private Double finishMass;
    private Double fixtureCost;
    private Double fixtureCostPerPart;
    private Double fullyBurdenedCost;
    private Double hardToolingCost;
    private Double laborCost;
    private Double laborTime;
    private Double lifetimeCost;
    private Double logisticsCost;
    private Double margin;
    private Double materialCost;
    private Double materialOverheadCost;
    private Double periodOverhead;
    private Double pieceCost;
    private Double programmingCost;
    private Double programmingCostPerPart;
    private Double roughMass;
    private Double scrapMass;
    private Double setupCostPerPart;
    private Double sgaCost;
    private Double totalCost;
    private Double otherDirectCosts;
}
