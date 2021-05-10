package com.apriori.bcs.entity.request;

import lombok.Data;

@Data
public class NewBatchProperties {
    private String externalId;
    private String rollupName;
    private String rollupScenarioName;
    private String exportSetName;
}
