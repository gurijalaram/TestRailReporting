package com.apriori.shared.util.models.response.component.componentiteration;

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
public class NoticeSpecificProperties {
    private String operationSequence;
    private Double current;
    private Double threadLength;
    private Double defaultThreadLength;
    private Double cadThreadLength;
    private Double suggested;
    private Double cycleTime;
    private Double cycleTimePercent;
    private String guidanceTopic;
    private String processGroup;
    private Boolean threaded;
    private Boolean cadThreaded;
}
