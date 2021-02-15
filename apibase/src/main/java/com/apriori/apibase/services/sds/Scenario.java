package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Schema(location = "sds/Scenario.json")
public class Scenario {

    private String costMaturity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String createdAt;
    private String customerIdentity;
    private String virtual;
    private String createdBy;
    private String identity;
    private String state;
    private String published;
    private String locked;
    private String scenarioName;
    private String ownedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String getVirtual() {
        return virtual;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getIdentity() {
        return identity;
    }

    public String getState() {
        return state;
    }

    public String getPublished() {
        return published;
    }

    public String getLocked() {
        return locked;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
