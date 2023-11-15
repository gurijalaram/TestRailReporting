package com.apriori.shared.util.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomAttributes {
    private String function;
    private String location;
    @JsonProperty(value = "UDA5")
    private List<String> uda5;
    @JsonProperty(value = "UDA3")
    private String uda3;
    @JsonProperty(value = "UDA2")
    private String uda2;
    @JsonProperty(value = "UDA1")
    private String uda1;
    private String department;
    private int workspaceId;
    private String defaultRole;
    private String grainingRequired;
    private String ccmRequired;
    private String accessGroup;
    private String assemblyOperationType;
    private List<Object> roles;
}
