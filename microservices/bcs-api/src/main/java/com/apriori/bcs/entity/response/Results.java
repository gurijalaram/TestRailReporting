package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZZZZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonRootName("response")
@Schema(location = "ResultsSchema.json")
public class Results {
    private String partIdentity;
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
    private String dfmRisk;
    private Double dfmScore;
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
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZZZZ.class)
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
    private Double partsPerHour;
    private Double periodOverhead;
    private Double pieceAndPeriod;
    private Double pieceCost;
    private String processGroupName;
    private Double productionLife;
    private Double programmingCost;
    private Double programmingCostPerPart;
    private Double roughMass;
    private String scenarioName;
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
    private String url;
    private Double utilization;
    private Double utilizationWithAddendum;
    private Double utilizationWithoutAddendum;
    private String vpeName;
    private Double width;
}
