package com.apriori.utils.authusercontext;

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
    private int workspaceId;
    private String defaultRole;
    private List<Object> roles;
    private String location;
    private String department;
}
