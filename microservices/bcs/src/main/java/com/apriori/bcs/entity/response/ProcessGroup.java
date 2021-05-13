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
    private List<String> vpeNames;
}
