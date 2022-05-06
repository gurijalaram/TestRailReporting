package com.apriori.acs.entity.request.workorders.publish.createpublishworkorder;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.upload.AssemblyComponent;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PublishInputs {
    private Boolean overwrite;
    private Boolean lock;
    private ScenarioIterationKey scenarioIterationKey;
    private List<AssemblyComponent> subComponents;
    private String description;
    private String comments;
}
