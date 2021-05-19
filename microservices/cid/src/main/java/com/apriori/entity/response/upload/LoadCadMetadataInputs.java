package com.apriori.entity.response.upload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoadCadMetadataInputs {
    private String fileMetadataIdentity;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
    private String requestedBy;
}
