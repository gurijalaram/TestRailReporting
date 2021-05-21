package com.apriori.entity.request.assemblyobjects;

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
