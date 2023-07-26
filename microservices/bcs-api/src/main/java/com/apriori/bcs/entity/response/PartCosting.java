package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonRootName("response")
@Schema(location = "PartCostingSchema.json")
public class PartCosting {
    public String partIdentity;
    public Double additionalAmortizedInvestment;
    public Double additionalDirectCosts;
    public Double amortizedInvestment;
    public Double annualCost;
    public Integer annualVolume;
    public Double batchCost;
    public Double batchSetupTime;
    public Integer batchSize;
    public Double cadSerLength;
    public Double cadSerWidth;
    public Double capitalInvestment;
    public String currencyCode;
    public Double cycleTime;
    public String dfmRisk;
    public Double directOverheadCost;
    public Integer dtcMessagesCount;
    public Double elapsedTime;
    public Double expendableToolingCostPerPart;
    public Double extraCosts;
    public Integer failedGcdsCount;
    public Integer failuresWarningsCount;
    public Double finalYield;
    public Double finishMass;
    public Double fixtureCost;
    public Double fixtureCostPerPart;
    public Double fullyBurdenedCost;
    public Integer gcdWithTolerancesCount;
    public Double goodPartYield;
    public Double hardToolingCost;
    public Double height;
    public Double laborCost;
    public Double laborTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime lastCosted;
    public Double length;
    public Double lifetimeCost;
    public Double logisticsCost;
    public Double margin;
    public Double marginPercent;
    public Double materialCost;
    public String materialName;
    public Double materialOverheadCost;
    public String materialStockFormName;
    public String materialStockName;
    public Double materialUnitCost;
    public Integer notSupportedGcdsCount;
    public Double numberOfParts;
    public Double numOperators;
    public Double numPartsPerSheet;
    public Double numScrapParts;
    public Double partsPerHour;
    public Double periodOverhead;
    public Double pieceAndPeriod;
    public Double pieceCost;
    public String processGroupName;
    public Double programmingCost;
    public Double programmingCostPerPart;
    public Double roughMass;
    public String scenarioName;
    public Double scrapMass;
    public Double setupCostPerPart;
    public Double sgaCost;
    public Double stockPropertyLength;
    public Double stockPropertyThickness;
    public Double stockPropertyWidth;
    public Double stripNestingPitch;
    public Double toolingCostPerPart;
    public Double totalCost;
    public Double totalProductionVolume;
    public Double otherDirectCosts;
    public String url;
    public Double utilization;
    public Double utilizationWithAddendum;
    public Double utilizationWithoutAddendum;
    public String vpeName;
    public Double width;
}
