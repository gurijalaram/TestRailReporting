package com.apriori.cidappapi.models.request;

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
    private String customAttributes;
    private String materialMode;
    private String materialName;
    private String processGroupName;
    private Double productionLife;
    @JsonProperty("vpeName")
    private String digitalFactory;
    private Boolean deleteTemplateAfterUse;
    private String costingTemplateIdentity;
    private List<String> propertiesToReset;
}
