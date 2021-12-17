package com.apriori.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BCSBatchDTO {

    private String batchId;
    private String environment;
    private String externalId;
    private String customerIdentity;
    private String rollupName;
    private String rollupScenarioName;
}
