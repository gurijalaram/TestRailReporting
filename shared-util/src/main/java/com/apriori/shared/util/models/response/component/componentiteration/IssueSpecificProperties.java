package com.apriori.shared.util.models.response.component.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String gcdList;
    private String tableType;
    private String panelOutput;
    private String unitType;
    private Integer current;
    private String holeCount;
    private List<HolesList> holesList;
    private Double suggested;
    private Double suggestedMax;
    private Integer suggestedMin;
    private String suggestedStock;
    private String edgeList;
    private String opName;
    private ProcName processName;
    private ProcName procName;
    private String cavityTexture;
    private String gcdType;
    private String guidanceTopic;
    private String bendHoleList;
    private List<GcdsToHighlight> gcdsToHighlight;
    private String holeSize;
    private String utilizationProcessName;
    private String thinnerStock;
    private String thickerStock;
    private String allStockThicknesses;
    private OdSuggest odSuggest;
    private String holeDiameter;
    private String holeLength;
    private String msgTolType;
    private String cheaperFinishOptions;
    private String toolHolderClearance;
    private String toolHolderObstructionHeight;
    private String toolDiameter;
    private String gcdHeight;
    private String opIndex;
    private String toolReach;
    private String roughingFeed;
    private String finishingFeed;
    private String finishingAxialEngagement;
    private String standardHolderClearanceInFront;
    private String slimProfileHolderClearanceBehind;
    private String standardHolderClearanceBehind;
    private String slimProfileHolderClearanceInFront;
    @JsonProperty("lToDRatio")
    private Double ltoDRatio;
}