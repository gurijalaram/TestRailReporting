package com.apriori.acs.api.models.response.workorders.upload;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class Assembly {
    private String scenarioName;
    private String fileKey;
    private String fileName;
    private Boolean keepFreeBodies;
    private Boolean freeBodiesPreserveCad;
    private Boolean freeBodiesIgnoreMissingComponent;
    private List<AssemblyComponent> subComponents;
}
