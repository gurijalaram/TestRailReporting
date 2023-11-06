package com.apriori.sds.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String processGroupName;
    private Double productionLife;
    private String vpeName;
    private Boolean deleteTemplateAfterUse;
    private String costingTemplateIdentity;
    private List<Object> propertiesToReset;
}
