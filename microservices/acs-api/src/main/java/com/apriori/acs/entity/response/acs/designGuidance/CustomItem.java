package com.apriori.acs.entity.response.acs.designGuidance;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CustomItem {
    private String gcdList;
    private String tableType;
    private String panelOutput;
    private String holeDiameter;
    private String holeSize;
    private String formHoleList;
    private String edgeList;
    private String holeLength;
    private Integer holeCount;
    private String utilizationProcessName;
    private String maxSectionHeightConversion;
    private ArrayList<GcdItem> gcdsToHighlight;
    private ProcessItem processName;
    private ProcessItem opName;
    private ProcessItem procName;
    private String materialYieldStrengthConversion;
}