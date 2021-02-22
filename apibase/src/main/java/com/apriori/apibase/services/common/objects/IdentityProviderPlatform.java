package com.apriori.apibase.services.common.objects;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentityProviderPlatform {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String description;
    private String name;
    private String vendor;
    private String identityProviderProtocol;

    public String getIdentity() {
        return identity;
    }

    public IdentityProviderPlatform setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public IdentityProviderPlatform setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IdentityProviderPlatform setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdentityProviderPlatform setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdentityProviderPlatform setName(String name) {
        this.name = name;
        return this;
    }

    public String getVendor() {
        return vendor;
    }

    public IdentityProviderPlatform setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getIdentityProviderProtocol() {
        return identityProviderProtocol;
    }

    public IdentityProviderPlatform setIdentityProviderProtocol(String identityProviderProtocol) {
        this.identityProviderProtocol = identityProviderProtocol;
        return this;
    }
}
