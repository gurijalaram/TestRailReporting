package com.apriori.cidappapi.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCostFailures {
    private String error;
    private String componentIdentity;
    private String componentName;
    private String scenarioIdentity;
    private String scenarioName;
}
