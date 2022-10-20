package com.apriori.acs.entity.response.workorders.simpleimagedata;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleImageDataInputs {
    private ScenarioIterationKey scenarioIterationKey;
}
