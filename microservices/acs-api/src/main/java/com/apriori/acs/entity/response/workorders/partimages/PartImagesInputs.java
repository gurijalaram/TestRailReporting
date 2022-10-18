package com.apriori.acs.entity.response.workorders.partimages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartImagesInputs {
    private String cadMetadataIdentity;
    private String requestedBy;
}
