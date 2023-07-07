package com.apriori.utils.common.customer.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DeploymentSchema.json")
public class Deployment {
    private Boolean isDefault;
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private String apVersion;
    private Boolean active;
    private List<Object> installations = null;
    private String customerIdentity;
    private String deploymentType;
}