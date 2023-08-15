package com.apriori.acs.models.response.workorders.upload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileUploadInputs {
    private String scenarioName;
    private String fileKey;
    private String fileName;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
    private List<AssemblyComponent> subComponents;
}
