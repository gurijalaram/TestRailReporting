package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/SiteApplicationSchema.json")
public class LicensedApplication {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String application;
    private String applicationIdentity;
    private LicensedApplication response;

    public String getIdentity() {
        return identity;
    }

    public LicensedApplication setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LicensedApplication setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LicensedApplication setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getApplication() {
        return application;
    }

    public LicensedApplication setApplication(String application) {
        this.application = application;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public LicensedApplication setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public LicensedApplication getResponse() {
        return response;
    }

    public LicensedApplication setResponse(LicensedApplication response) {
        this.response = response;
        return this;
    }
}