
package com.apriori.cis.api.models.response.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ComponentParameters {
    private String componentIdentity;
    private String iterationIdentity;
    private String scenarioIdentity;
}