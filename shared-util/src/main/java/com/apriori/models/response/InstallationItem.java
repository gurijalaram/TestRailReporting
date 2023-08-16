package com.apriori.models.response;

import lombok.Data;

import java.util.List;

@Data
public class InstallationItem {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private String apVersion;
    private String name;
    private String cloudReference;
    private String description;
    private Boolean active;
    private String realm;
    private String url;
    private String s3Bucket;
    private String tenantGroup;
    private String tenant;
    private String clientId;
    private String clientSecret;
    private String cidGlobalKey;
    private String cidApiId;
    private String cidApiSecret;
    private String customerIdentity;
    private List<ApplicationItem> applications;
    private String region;
}
