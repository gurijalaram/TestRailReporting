package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "cds/RoleSchema.json")
public class Role {
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private Role response;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    public Role getResponse() {
        return this.response;
    }

    public Role setResponse(Role response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Role setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Role setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Role setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Role setDescription(String description) {
        this.description = description;
        return this;
    }
}
