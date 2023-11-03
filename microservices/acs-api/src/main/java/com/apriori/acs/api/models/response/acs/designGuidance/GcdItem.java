package com.apriori.acs.api.models.response.acs.designGuidance;

import lombok.Data;

@Data
public class GcdItem {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;
}