package com.apriori.acs.entity.response.workorders.generatepartimages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeneratePartImagesOutputs {
    private String cadMetadataIdentity;
    private String webImageIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
}
