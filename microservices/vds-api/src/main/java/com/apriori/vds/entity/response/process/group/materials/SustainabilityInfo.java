package com.apriori.vds.entity.response.process.group.materials;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SustainabilityInfo {
    private String identity;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private Double hexBarCarbonEmissionsFactor;
    private Double rectangularBarCarbonEmissionsFactor;
    private Double roundBarCarbonEmissionsFactor;
    private Double roundTubeCarbonEmissionsFactor;
    private Double squareBarCarbonEmissionsFactor;
    private Double carbonEmissionsFactor;
}