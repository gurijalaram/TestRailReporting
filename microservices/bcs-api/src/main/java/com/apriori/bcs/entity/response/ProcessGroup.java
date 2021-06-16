package com.apriori.bcs.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessGroup {
    private String identity;
    private String name;
    private String description;
    private Boolean cidSupported;
    private Boolean assemblySupported;
    private Boolean partSupported;
    private Boolean secondaryProcessGroup;
    private String defaultVpeName;
    private List<String> vpeNames;
}
