package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SiteSchema.json")
public class Site {
    private Site response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private String name;
    private String description;
    private String siteId;
    private Boolean active;
    private List<Deployment> deployments;
    private String createdByName;

    public Site getResponse() {
        return response;
    }

    public Site setResponse(Site response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public Site setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Site setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Site setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Site setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
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

    public String getSiteId() {
        return siteId;
    }

    public Site setSiteId(String siteId) {
        this.siteId = siteId;
        return this;
    }


    public Boolean getActive() {
        return active;
    }

    public Site setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public List<Deployment> getDeployments() {
        return deployments;
    }

    public Site setDeployments(List<Deployment> deployments) {
        this.deployments = deployments;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public Site setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }
}