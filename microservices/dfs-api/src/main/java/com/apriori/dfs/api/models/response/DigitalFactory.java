package com.apriori.dfs.api.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(location = "DigitalFactorySchema.json")
public class DigitalFactory {

    private String identity;

    private Integer annualVolume;

    private Integer batchesPerYear;

    private String currencyCode;

    private String customerIdentity;

    private String description;

    private String location;

    private String name;

    private List<String> processGroups;

    private Integer productionLife;

    private String revision;

    private String type;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;

    private String createdBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;

    private String updatedBy;
}
