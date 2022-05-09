package com.apriori.css.entity.response.componentiteration;

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
