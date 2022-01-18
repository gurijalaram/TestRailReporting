package com.apriori.cidappapi.entity.response.scenarios;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "CopyScenario.json")
@Data
@Builder
@JsonRootName(value = "response")
public class CopyScenarioResponse {

    private String createdBy;
    private String createdByName;
    private String customerIdentity;
    private String identity;
    private String lastAction;
    private Boolean locked;
    private String ownedBy;
    private String ownedByName;
    private String previousScenarioState;
    private Boolean published;
    private String scenarioName;
    private String scenarioState;
    private String scenarioType;
    private Boolean systemLocked;
    private List<Permissions> permissions;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSLetterZ.class)
    private LocalDateTime createdAt;



}
