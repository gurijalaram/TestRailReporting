package com.apriori.acs.models.request.workorders.assemblyobjects;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AssemblyInfoComponent extends AssemblyInfo {
    private String componentName;
}
