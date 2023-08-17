package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutingNodeOptions {
    @JsonProperty("DigitalFactoryName")
    private String digitalFactoryName;
    @JsonProperty("Identity")
    private String identity;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("ProcessGroupName")
    private String processGroupName;
}
