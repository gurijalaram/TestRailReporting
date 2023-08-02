package com.apriori.dms.models.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DmsAttributes {
    private String attribute;
    private String attributeDisplayValue;
    private String bidPackageIdentity;
    private String componentIdentity;
    private String componentType;
    private String customerIdentity;
    private String models;
    private String pinDetails;
    private String projectIdentity;
    private String projectItemIdentity;
    private String scenarioDiscussionIdentity;
    private String scenarioIdentity;
    private String subject;
    private String type;
    private String cadModelSnapshot;
    private String gcds;
    private String attributeCategory;
    private String processIdentity;
    private String description;
}
