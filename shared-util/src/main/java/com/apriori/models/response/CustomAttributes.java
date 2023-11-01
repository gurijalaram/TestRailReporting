package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private List<String> UDA5;
    private String department;
    private int workspaceId;
    private String defaultRole;
    private String assemblyOperationType;
    private List<Object> roles;
}
