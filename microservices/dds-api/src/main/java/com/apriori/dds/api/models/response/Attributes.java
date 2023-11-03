package com.apriori.dds.api.models.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attributes {
    private String attribute;
    private String bidPackageIdentity;
    private String componentIdentity;
    private String customerIdentity;
    private String projectIdentity;
    private String projectItemIdentity;
    private String scenarioDiscussionIdentity;
    private String scenarioIdentity;
    private String subject;
    private String type;
    private String componentType;
    private String gcds;
    private String pinDetails;
}
