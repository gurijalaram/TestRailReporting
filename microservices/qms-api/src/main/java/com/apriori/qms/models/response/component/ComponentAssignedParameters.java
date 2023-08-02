package com.apriori.qms.models.response.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
public class ComponentAssignedParameters {
    private String componentIdentity;
    private String iterationIdentity;
    private String scenarioIdentity;
}
