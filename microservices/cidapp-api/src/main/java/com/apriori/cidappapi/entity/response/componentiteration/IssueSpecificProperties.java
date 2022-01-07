package com.apriori.cidappapi.entity.response.componentiteration;

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
public class IssueSpecificProperties  {
    private String tableType;
    private String panelOutput;
    private String unitType;
    private Integer current;
    private Integer suggestedMin;
    private String edgeList;
    private OpName opName;
    private ProcName procName;
    private String gcdType;
}