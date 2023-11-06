package com.apriori.cic.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostingInputs {
    private String processGroupName;
    private String materialName;
    private String vpeName;
    private String scenarioName;
    private Integer annualVolume;
    private Integer batchSize;
    private String description;
    private Integer productionLife;
    @JsonProperty("UDA1")
    private String customString;
    @JsonProperty("UDA2")
    private String customNumber;
    @JsonProperty("UDA3")
    private String customDate;
    @JsonProperty("UDA4")
    private String customMulti;
    @JsonProperty("WH_REEL_UNIT_TYPE")
    private String nonSearchUda;
}
