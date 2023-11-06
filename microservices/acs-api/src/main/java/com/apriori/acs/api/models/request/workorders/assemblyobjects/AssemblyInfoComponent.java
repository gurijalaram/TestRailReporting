package com.apriori.acs.api.models.request.workorders.assemblyobjects;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AssemblyInfoComponent extends AssemblyInfo {
    private String componentName;
}
