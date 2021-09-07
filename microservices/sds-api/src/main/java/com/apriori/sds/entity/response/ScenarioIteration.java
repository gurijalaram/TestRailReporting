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

@Schema(location = "ScenarioIterationResponse.json")
@Data
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioIteration {
    private String customerIdentity;
    private Object scenarioProcesses;
    private String hasThumbnail;
    private String costingMessage;
    private String createdBy;
    private String identity;
    private String iteration;
    private String hasWebImage;
    private Object scenarioDtcIssues;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
