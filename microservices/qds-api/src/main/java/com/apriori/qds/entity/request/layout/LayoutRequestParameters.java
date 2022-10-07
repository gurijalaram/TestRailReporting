package com.apriori.qds.entity.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayoutRequestParameters {
    private Boolean published;
    private String applicationIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String name;
}
