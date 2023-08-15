package com.apriori.ach.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class DeploymentAch {
    private String identity;
    private String type;
    private String name;
    private String description;
    private String apVersion;
    private List<Application> applications;
}