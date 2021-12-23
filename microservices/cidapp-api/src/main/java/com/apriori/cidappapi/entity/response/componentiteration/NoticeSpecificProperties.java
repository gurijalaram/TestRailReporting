package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticeSpecificProperties {
    private String operationSequence;
    private Integer current;
    private Integer threadLength;
    private Integer defaultThreadLength;
    private Integer cadThreadLength;
    private Integer suggested;
    private Integer cycleTime;
    private Integer cycleTimePercent;
    private String guidanceTopic;
    private String processGroup;
    private Boolean threaded;
    private Boolean cadThreaded;
}
