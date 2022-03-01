package com.apriori.acs.entity.response.workorders.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class GenerateAssemblyImagesOutputs {
    private String cadMetadataIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String productStructureIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
    private ArrayList<String> generatedWebImages;
}
