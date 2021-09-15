package com.apriori.cidappapi.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostRequest {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private Object customAttributes;
    private String materialName;
    private String processGroupName;
    private Double productionLife;
    private String vpeName;
    private Boolean deleteTemplateAfterUse;
    private String costingTemplateIdentity;

    @JsonProperty("UDARegion")
    private String udaRegion;
}
