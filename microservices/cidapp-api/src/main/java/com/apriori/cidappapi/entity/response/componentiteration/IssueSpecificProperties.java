package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}