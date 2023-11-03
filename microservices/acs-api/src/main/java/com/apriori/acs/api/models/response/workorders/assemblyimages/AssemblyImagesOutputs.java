package com.apriori.acs.api.models.response.workorders.assemblyimages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class AssemblyImagesOutputs {
    private String cadMetadataIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String productStructureIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
    private ArrayList<String> generatedWebImages;
}
