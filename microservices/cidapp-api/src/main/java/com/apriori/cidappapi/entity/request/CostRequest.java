package com.apriori.cidappapi.entity.request;

import com.apriori.utils.enums.DigitalFactoryEnum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String materialMode;
    private String materialName;
    private String processGroup;
    private Double productionLife;
    private DigitalFactoryEnum digitalFactory;
    private Boolean deleteTemplateAfterUse;
    private String costingTemplateIdentity;
    private List<Object> propertiesToReset;

    @JsonProperty("UDARegion")
    private String udaRegion;
}
