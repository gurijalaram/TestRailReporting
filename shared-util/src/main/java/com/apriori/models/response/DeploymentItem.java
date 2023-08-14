package com.apriori.models.response;

import lombok.Data;

import java.util.List;

@Data
public class DeploymentItem {
    private Boolean isDefault;
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private String apVersion;
    private Boolean active;
    private List<InstallationItem> installations;
    private String customerIdentity;
    private String deploymentType;
}
