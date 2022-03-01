package com.apriori.acs.entity.response.workorders.upload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratePartImagesInputs {
    private String cadMetadataIdentity;
    private String requestedBy;
}
