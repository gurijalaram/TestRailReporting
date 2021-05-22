package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/BatchItemSchema.json")
public class BatchItem {
    private BatchItem response;
    private String identity;
    private String id;
    private String customerIdentity;
    private String batchIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String userName;
    private String givenName;
    private String familyName;
    private String prefix;
    private String suffix;
    private String jobTitle;
    private String email;
    private String department;
    private String cityTown;
    private String stateProvince;
    private String county;
    private String countryCode;
    private String timezone;
    private String createdByName;
    private String cdsStatus;

    public BatchItem getResponse() {
        return response;
    }

    public String getIdentity() {
        return identity;
    }

    public String getId() {
        return id;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String getBatchIdentity() {
        return batchIdentity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUserName() {
        return userName;
    }

    public BatchItem setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public BatchItem setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public BatchItem setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public BatchItem setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public BatchItem setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getCityTown() {
        return cityTown;
    }

    public BatchItem setCityTown(String cityTown) {
        this.cityTown = cityTown;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getCounty() {
        return county;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public String getCdsStatus() {
        return cdsStatus;
    }
}
