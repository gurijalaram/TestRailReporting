package com.apriori.acs.entity.response.acs.artifactproperties;

import lombok.Data;

@Data
public class StatusItem {
    private String severity;
    private Boolean dirty;
    private Boolean locked;
    private Boolean initialized;
    private String overrideStatus;
}