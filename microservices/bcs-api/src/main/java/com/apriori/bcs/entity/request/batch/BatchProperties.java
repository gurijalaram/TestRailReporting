package com.apriori.bcs.entity.request.batch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchProperties {
    private String externalId;
    private String rollupName;
    private String rollupScenarioName;
    private String exportSetName;
}
