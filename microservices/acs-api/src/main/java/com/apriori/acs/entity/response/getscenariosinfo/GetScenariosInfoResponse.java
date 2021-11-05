package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.acs.entity.response.createmissingscenario.ScenarioIterationKey;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "GetScenariosInfoResponse.json")
public class GetScenariosInfoResponse extends ArrayList<GetScenariosInfoResponse> {
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
