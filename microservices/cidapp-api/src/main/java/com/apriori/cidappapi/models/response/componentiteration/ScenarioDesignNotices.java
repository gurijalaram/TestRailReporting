package com.apriori.cidappapi.models.response.componentiteration;

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
public class ScenarioDesignNotices {
    private String identity;
    private String attribute;
    private String attributeDisplayName;
    private String category;
    private String categoryDisplayName;
    private String gcdArtifactType;
    private String gcdDisplayName;
    private Integer gcdSequenceNumber;
    private NoticeSpecificProperties noticeSpecificProperties;
}
