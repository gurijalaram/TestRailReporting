package com.apriori.acs.models.response.acs.costresults;

import lombok.Data;

@Data
public class ProcessInstanceKey {
    private String processGroupName;
    private String processName;
    private Integer index;
    private String displayName;
}
