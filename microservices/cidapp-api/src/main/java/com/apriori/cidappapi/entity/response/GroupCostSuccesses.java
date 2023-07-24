package com.apriori.cidappapi.entity.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCostSuccesses {
    private String identity;
    private String createdBy;
    private String updatedBy;
    private String createdByName;
    private String updatedByName;
    private List<String> permissions;
    private String customerIdentity;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private String previousScenarioState;
    private String lastAction;
    private String ownedBy;
    private Boolean locked;
    private String published;
    private String systemLocked;
    private String ownedByName;
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
}
