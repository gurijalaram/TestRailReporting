package com.apriori.models.response.component.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioDtcIssue {
    private String identity;
    private String attributeId;
    private String attributeText;
    private String category;
    private String categoryText;
    private Integer decimalPlaces;
    private String dtcIssueType;
    private String gcdArtifactTypeName;
    private String gcdDisplayName;
    private Integer gcdSequenceNumber;
    private String messageText;
    private Integer priority;
    private String type;
    private String currentValueText;
    private String messageId;
    private String processDisplayName;
    private String processGroupName;
    private Integer processIndex;
    private String processName;
    private String rule;
    private String suggestedMinValueText;
    private String vpeName;
    private Double currentValue;
    private Double suggestedMinValue;
    private String unitType;
}
