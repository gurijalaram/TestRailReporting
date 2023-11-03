package com.apriori.acs.api.models.response.acs.scenariosinfo;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/ScenariosInfoResponse.json")
public class ScenariosInfoResponse extends ArrayList<ScenariosInfoItem> {
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
