package com.apriori.entity.response.upload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoadCadMetadataOutputs {
    private String cadMetadataIdentity;
}
