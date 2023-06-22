package com.apriori.acs.entity.response.acs.designGuidance;

import lombok.Data;

@Data
public class CustomItem {
    private String tableType;
    private String panelOutput;
    private String utilizationProcessName;
    private ProcessItem processName;
}