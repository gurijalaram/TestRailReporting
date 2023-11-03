package com.apriori.acs.api.models.response.acs.designGuidance;

import lombok.Data;

@Data
public class ProcessItem {
    private String processGroupName;
    private String processName;
    private Integer index;
    private String displayName;
}
