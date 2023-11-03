package com.apriori.dms.api.models.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DmsAttributesRequest {
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
}
