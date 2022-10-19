package com.apriori.acs.entity.response.workorders.assemblyimages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssemblyImagesSubComponent {
    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
}
