package com.apriori.models.response;

import lombok.Data;

@Data
public class ApplicationItem {
    private Boolean isCloudHomeApp;
    private Boolean isSingleTenant;
    private Boolean isPublic;
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String cloudReference;
    private String serviceName;
    private String description;
    private String iconUrl;
}
