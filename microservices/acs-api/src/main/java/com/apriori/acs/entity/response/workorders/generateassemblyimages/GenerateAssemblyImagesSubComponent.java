package com.apriori.acs.entity.response.workorders.generateassemblyimages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateAssemblyImagesSubComponent {
    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
}
