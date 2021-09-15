package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "ScenarioResponse.json")
@Data
@JsonRootName("response")
public class Scenario {
    private String lastAction;
    private Boolean systemLocked;
    private String subjectIdentity;
    private String costMaturity;
    private String customerIdentity;
    private String virtual;
    private String createdBy;
    private String updatedBy;
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
    private List<String> permissions;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
