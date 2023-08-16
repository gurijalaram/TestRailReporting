package com.apriori.acs.models.response.workorders.upload;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadOutputs {
    private ScenarioIterationKey scenarioIterationKey;
}
