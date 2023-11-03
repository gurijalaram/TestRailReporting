package com.apriori.acs.api.models.response.workorders.upload;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadOutputs {
    private ScenarioIterationKey scenarioIterationKey;
}
