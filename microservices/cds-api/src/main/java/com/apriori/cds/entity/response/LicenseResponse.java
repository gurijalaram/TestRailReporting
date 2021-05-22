package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/LicenseResponseSchema.json")
public class LicenseResponse {
    private LicenseResponse response;
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private Boolean active;
    private String apVersion;
    private String description;
    private List<SubLicense> subLicenses = null;
    private List<LicensedModule> licensedModules = null;
    private String customerIdentity;
    private String siteIdentity;

    public LicenseResponse getResponse() {
        return response;
    }

    public LicenseResponse setResponse(LicenseResponse response) {
        this.response = response;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public String getApVersion() {
        return apVersion;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentity() {
        return identity;
    }

    public List<LicensedModule> getLicensedModules() {
        return licensedModules;
    }

    public String getSiteIdentity() {
        return siteIdentity;
    }

    public List<SubLicense> getSubLicenses() {
        return subLicenses;
    }
}
