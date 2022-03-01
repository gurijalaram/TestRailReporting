package com.apriori.acs.entity.request.workorders.assemblyobjects;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AssemblyComponent extends Assembly {
    private String componentName;
}
