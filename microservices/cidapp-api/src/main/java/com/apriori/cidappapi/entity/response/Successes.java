package com.apriori.cidappapi.entity.response;

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
public class Successes {
    private String iterationIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String filename;
    private String identity;
    private String resourceName;
    private String scenarioName;
}
