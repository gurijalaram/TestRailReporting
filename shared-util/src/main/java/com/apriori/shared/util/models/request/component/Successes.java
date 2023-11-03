package com.apriori.shared.util.models.request.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
