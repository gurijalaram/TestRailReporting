package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioDesignIssues {
    private String identity;
    private String attribute;
    private String attributeDisplayName;
    private String category;
    private String categoryDisplayName;
    private String issueDescription;
    private String issueSeverity;
    private IssueSpecificProperties issueSpecificProperties;
    private String primaryGcdArtifactType;
    private String primaryGcdDisplayName;
    private String primaryGcdSequenceNumber;
    private String processGroupName;
    private String processName;
    private String secondaryGcdArtifactType;
    private String secondaryGcdDisplayName;
    private Integer secondaryGcdSequenceNumber;
}
