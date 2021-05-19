package com.apriori.entity.response.upload;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class GenerateAssemblyImagesOutputs {
    private String cadMetadataIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String productStructureIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
    private ArrayList<String> generatedWebImages;
}
