package com.apriori.qms.api.models.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutConfigurationParameters {
    private String configuration;
    private String name;
    private Boolean shareable;
    private String deploymentIdentity;
    private String installationIdentity;
}
