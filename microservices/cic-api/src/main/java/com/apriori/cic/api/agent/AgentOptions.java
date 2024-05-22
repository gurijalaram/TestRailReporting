package com.apriori.cic.api.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AgentOptions {
    private String agentFieldName;
    private String agentFieldValue;
}
