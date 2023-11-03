package com.apriori.acs.api.models.response.acs.scenariosinfo;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;

@Data
public class ScenariosInfoItem {
    public Boolean initialized;
    public Boolean missing;
    public Boolean virtual;
    public String componentName;
    public String componentType;
    public String scenarioName;
    public Boolean locked;
    public String fileName;
    public String fileMetadataIdentity;
    public String createdAt;
    public String createdBy;
    public String updatedAt;
    public String updatedBy;
    public ScenarioIterationKey scenarioIterationKey;
    public String configurationName;
}
