package com.apriori.cidappapi.models.response.scenarios;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(location = "ScenarioResponse.json")
public class ScenarioResponse {
    private String assignedBy;
    private String assignedByName;
    private String assignedTo;
    private String assignedToName;
    private String costMaturity;
    private String createdBy;
    private String createdByName;
    private String customerIdentity;
    private String description;
    private String notes;
    private String identity;
    private String lastAction;
    private Boolean locked;
    private String ownedBy;
    private String ownedByName;
    private String previousScenarioState;
    private Boolean published;
    private String publishedBy;
    private String publishedByName;
    private String scenarioName;
    private String scenarioState;
    private String scenarioType;
    private String status;
    private String subjectIdentity;
    private Boolean systemLocked;
    private String updatedBy;
    private String updatedByName;
    private String lockedBy;
    private String lockedByName;
    private List<String> permissions;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime assignedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime publishedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime updatedAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime lockedAt;
}
