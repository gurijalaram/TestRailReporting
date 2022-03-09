package com.apriori.acs.entity.response.workorders.generatesimpleimagedata;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateSimpleImageDataInputs {
    private ScenarioIterationKey scenarioIterationKey;
}
