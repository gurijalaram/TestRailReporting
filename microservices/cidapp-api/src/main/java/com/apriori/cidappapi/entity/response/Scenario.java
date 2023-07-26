package com.apriori.cidappapi.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    private Boolean published;
    private Integer annualVolume;
    private Integer batchSize;
    private String materialMode;
    private String materialName;
    private String processGroupName;
    private Integer productionLife;
    private String vpeName;
    private List<String> propertiesToReset;
    private List<String> routingNodeOptions;
    private Boolean locked;
    private String ownedByName;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private String ownedBy;
    private String previousScenarioState;
    private String notes;
    private String description;
    private String createdByName;
    private String updatedByName;
    private String status;
    private String assignedTo;
    private String assignedBy;
    private String assignedToName;
    private String assignedByName;
    private List<String> permissions;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime assignedAt;
}
