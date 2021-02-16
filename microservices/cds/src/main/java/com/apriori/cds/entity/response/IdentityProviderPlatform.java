
package com.apriori.cds.entity.response;

import java.util.HashMap;
import java.util.Map;

public class IdentityProviderPlatform {

    private String identity;
    private String createdBy;
    private String createdAt;
    private String description;
    private String name;
    private String vendor;
    private String identityProviderProtocol;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getIdentityProviderProtocol() {
        return identityProviderProtocol;
    }

    public void setIdentityProviderProtocol(String identityProviderProtocol) {
        this.identityProviderProtocol = identityProviderProtocol;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
