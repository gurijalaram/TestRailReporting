package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@JsonRootName("response")
@Schema(location = "CustomerBatchResponseSchema.json")
public class Batch {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String state;
    private String externalId;
    private String componentName;
    private String scenarioName;
    private String errors;
    private String rollupName;
    private String rollupScenarioName;
    private String exportSetName;
    private Long costingDuration;

    public Batch setCostingDuration(LocalDateTime updatedTime) {
        this.costingDuration = ChronoUnit.SECONDS.between(createdAt, updatedTime);
        return this;
    }
}
