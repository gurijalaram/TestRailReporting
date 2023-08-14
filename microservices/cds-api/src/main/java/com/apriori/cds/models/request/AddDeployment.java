package com.apriori.cds.models.request;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "AddDeploymentSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonRootName("response")
public class AddDeployment {
    private AddDeployment deployment;
    private String name;
    private String description;
    private String deploymentType;
    private String siteIdentity;
    private String active;
    private String isDefault;
    private String createdBy;
    private String apVersion;
    private List<String> applications = null;
}