package com.apriori.cisapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupItems {
    private String componentIdentity;
    private String scenarioIdentity;
}
