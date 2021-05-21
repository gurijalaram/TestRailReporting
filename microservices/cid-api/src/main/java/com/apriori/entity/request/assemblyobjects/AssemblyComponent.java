package com.apriori.entity.request.assemblyobjects;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AssemblyComponent extends Assembly {
    private String componentName;
}
