package com.apriori.models.response;

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
public class ScenarioIterationKey {
    private String typeName;
    private Integer iteration;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}
