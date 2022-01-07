package com.apriori.cidappapi.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostRequest {
    private String identity;
    private Integer annualVolume;
    private Integer batchSize;
    private Object customAttributes;
    private String materialMode;
    private String materialName;
    private String processGroupName;
    private Double productionLife;
    @JsonProperty("vpeName")
    private String digitalFactory;
    private Boolean deleteTemplateAfterUse;
    private String costingTemplateIdentity;
    private List<Object> propertiesToReset;
    @JsonProperty("UDARegion")
    private String udaRegion;
}
