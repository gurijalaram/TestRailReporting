package com.apriori.entity.response.upload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadInputs {
    private String scenarioName;
    private String fileKey;
    private String fileName;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
}
