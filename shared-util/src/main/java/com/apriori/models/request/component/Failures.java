package com.apriori.models.request.component;

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
public class Failures {
    private String error;
    private String filename;
    private String identity;
    private String resourceName;
    private String scenarioName;
    private String componentIdentity;
    private String scenarioIdentity;
    private String componentName;
    private String scenarioAssociationIdentity;
}
