package com.apriori.acs.entity.response.workorders.generateassemblyimages;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GenerateAssemblyImagesInputs {
    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
    private List<GenerateAssemblyImagesSubComponent> subComponents;
    private String requestedBy;
}
