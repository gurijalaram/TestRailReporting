package com.apriori.acs.entity.response.workorders.upload;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadOutputs {
    private ScenarioIterationKey scenarioIterationKey;
}
