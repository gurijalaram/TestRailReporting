package com.apriori.acs.models.response.workorders.partimages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartImagesOutputs {
    private String cadMetadataIdentity;
    private String webImageIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
}
