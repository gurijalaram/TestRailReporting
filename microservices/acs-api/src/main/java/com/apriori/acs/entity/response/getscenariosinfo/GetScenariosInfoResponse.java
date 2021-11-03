package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.acs.entity.response.createmissingscenario.ScenarioIterationKey;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Data
@Schema(location = "GetScenariosInfoResponse.json")
public class GetScenariosInfoResponse {
    public boolean initialized;
    public boolean missing;
    public boolean virtual;
    public String componentName;
    public String componentType;
    public String scenarioName;
    public boolean locked;
    public String fileName;
    public String fileMetadataIdentity;
    public String createdAt;
    public String createdBy;
    public String updatedAt;
    public String updatedBy;
    public ScenarioIterationKey scenarioIterationKey;
}
