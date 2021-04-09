package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(location = "sds/ScenarioIterationResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioIteration extends JacksonUtil {
    private ScenarioIteration response;
    private String customerIdentity;
    private Object scenarioProcesses;
    private String hasThumbnail;
    private String costingMessage;
    private String createdBy;
    private String identity;
    private String iteration;
    private String hasWebImage;
    private Object scenarioDtcIssues;
    private Object scenarioCustomAttributes;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
}
