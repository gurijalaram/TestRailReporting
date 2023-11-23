package com.apriori.cid.api.models.response.scenarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteSuccesses {
    private String componentIdentity;
    private String scenarioIdentity;
}
