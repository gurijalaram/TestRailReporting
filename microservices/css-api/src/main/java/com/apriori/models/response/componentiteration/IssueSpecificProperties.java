package com.apriori.models.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IssueSpecificProperties  {
    private String tableType;
    private String holeCount;
    private String panelOutput;
    private String unitType;
    private String thickerStock;
    private String utilizationProcessName;
    private Integer current;
    private Double suggested;
    private Integer suggestedMin;
    private String edgeList;
    private String holeSize;
    private String suggestedStock;
    private String thinnerStock;
    private String allStockThicknesses;
    private OpName opName;
    private ProcName procName;
    private String gcdType;
    private String guidanceTopic;
    private String bendHoleList;
    private List<GcdsToHighlight> gcdsToHighlight;
}