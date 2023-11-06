package com.apriori.acs.api.models.response.workorders.simpleimagedata;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleImageDataInputs {
    private ScenarioIterationKey scenarioIterationKey;
}
