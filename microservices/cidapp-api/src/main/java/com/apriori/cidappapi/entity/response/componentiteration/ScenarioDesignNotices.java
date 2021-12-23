package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
