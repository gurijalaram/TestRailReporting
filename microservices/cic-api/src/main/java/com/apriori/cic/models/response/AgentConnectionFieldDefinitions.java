package com.apriori.cic.models.response;

import com.apriori.cic.models.request.FieldDefinitionKey;
import com.apriori.cic.models.request.FieldDefinitionValue;

import lombok.Data;

@Data
public class AgentConnectionFieldDefinitions {
    private FieldDefinitionKey appKey;
    private FieldDefinitionValue connectionInfo;
}
