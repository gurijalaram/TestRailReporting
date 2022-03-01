package com.apriori.acs.entity.request.workorders.assemblyobjects;

import lombok.Data;

import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Data
@SuperBuilder
public class Assembly {
    private String assemblyName;
    private String scenarioName;
    private String processGroup;
    private ArrayList<AssemblyComponent> components;
}
