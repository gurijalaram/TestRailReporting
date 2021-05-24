package com.apriori.entity.response.upload;

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
