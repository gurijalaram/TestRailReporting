package com.apriori.entity.request.assemblycomponentobject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssemblyComponent {
    private String componentName;
    private String scenarioName;
    private String processGroup;
}
