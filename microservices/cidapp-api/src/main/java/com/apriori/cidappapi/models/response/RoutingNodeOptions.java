package com.apriori.cidappapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutingNodeOptions {
    private String identity;
    private String digitalFactoryName;
    private String name;
    private String processGroupName;
}
