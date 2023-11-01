package com.apriori.models.response;

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
    private String department;
    private int workspaceId;
    private String defaultRole;
    private String assemblyOperationType;
    private List<Object> roles;
}
