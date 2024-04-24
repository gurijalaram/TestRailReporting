package com.apriori.shared.util.models.request.component;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("name")
    private String routing;
    private String processGroupName;
}
