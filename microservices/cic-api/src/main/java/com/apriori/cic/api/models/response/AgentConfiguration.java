package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "AgentConfigurationSchema.json")
public class AgentConfiguration {
    public String hostName;
    public String rootFolderPath;
    public int csrfTokenTimeoutSeconds;
    public int maxPartsToReturn;
    public String cicHostUrl;
    public int scanRate;
    public int reconnectionInterval;
    public String plmType;
}
