package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "ScenarioResponse.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {
    private String costMaturity;
    private String customerIdentity;
    private String virtual;
    private String createdBy;
    private String identity;
    private String state;
    private String published;
    private String locked;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private String ownedBy;
    private String previousScenarioState;
    private String notes;
    private String description;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
