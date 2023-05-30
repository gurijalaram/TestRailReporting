package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioProcess {
    private String identity;
    private Double capitalInvestment;
    private Boolean costingFailed;
    private Double cycleTime;
    private String displayName;
    private Double fullyBurdenedCost;
    private String machineName;
    private Integer order;
    private String processGroupName;
    private String processName;
    private Double totalCost;
    private Double additionalDirectCosts;
    private Double amortizedInvestment;
    private Double annualCost;
    private Double batchCost;
    private Double batchSetupTime;
    private Double directOverheadCost;
    private Double expendableToolingCostPerPart;
    private Double extraCosts;
    private Double finishMass;
    private Double fixtureCost;
    private Double fixtureCostPerPart;
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
    private Double otherDirectCosts;
    private Double directOverheadRate;
    private Double indirectOverheadPercent;
    private Double hoursPerShift;
    private Double goodPartYield;
    private Double finalYield;
    private Double indirectOverheadRate;
    private Double laborRate;
    private Double machineLife;
    private Double machinePower;
    private Double machinePrice;
    private Double machineUptime;
    private Double numOperators;
    private Double numScrapParts;
    private Double overheadRate;
    private Double pieceAndPeriod;
    private Double sgaPercent;
    private Double shiftsPerDay;
    private Double toolingCostPerPart;
    private Double totalMachineCost;
    private Double productionDaysPerYear;
    private String vpeName;
    private String energyCarbonFactor;
    private String primaryMaterialCarbonFactor;
    private String logisticsCarbon;
    private String materialCarbon;
    private String processCarbon;
    private String supportsSustainabilityType;
    private String totalCarbon;
    private String annualManufacturingCarbon;
    private List<Gcds> gcds;
}
