package com.apriori.acs.api.models.request.workorders.assemblyobjects;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Data
@SuperBuilder
public class AssemblyInfo {
    private String assemblyName;
    private String scenarioName;
    private String processGroup;
    private ArrayList<AssemblyInfoComponent> components;
}
