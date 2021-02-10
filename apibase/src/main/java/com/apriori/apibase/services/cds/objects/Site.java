package com.apriori.apibase.services.cds.objects;

import com.apriori.utils.http.enums.Schema;

import java.util.List;
@Schema(location = "cds/SiteSchema.json")
public class Site {

    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private Boolean active;
    private String siteId;
    private String customerIdentity;
    private List<Deployment> deployments;

    public String getIdentity() {
        return identity;
    }

    public Site setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Site setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Site setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return name;
    }

    public Site setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Site setDescription(String description) {
        this.description = description;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Site setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getSiteId() {
        return siteId;
    }

    public Site setSiteId(String siteId) {
        this.siteId = siteId;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Site setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public List<Deployment> getDeployments() {
        return deployments;
    }

    public Site setDeployments(List<Deployment> deployments) {
        this.deployments = deployments;
        return this;
    }

}
