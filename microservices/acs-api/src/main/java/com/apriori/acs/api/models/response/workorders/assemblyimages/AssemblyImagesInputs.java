package com.apriori.acs.api.models.response.workorders.assemblyimages;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssemblyImagesInputs {
    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
    private List<AssemblyImagesSubComponent> subComponents;
    private String requestedBy;
}
