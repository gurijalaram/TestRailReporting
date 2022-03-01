package com.apriori.acs.entity.response.workorders.upload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GenerateAssemblyImagesInputs {
    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
    private String requestedBy;
    private List<GenerateAssemblyImagesInputs> subComponents;
}
