package com.apriori.cic.api.models.response;

import com.apriori.cic.api.models.request.FieldDefinitionKey;
import com.apriori.cic.api.models.request.FieldDefinitionValue;

import lombok.Data;

@Data
public class AgentConnectionFieldDefinitions {
    private FieldDefinitionKey appKey;
    private FieldDefinitionValue connectionInfo;
}
