package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.entity.response.upload.ScenarioIterationKey;

import lombok.Data;

@Data
public class GetScenariosInfoItem {
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
}
