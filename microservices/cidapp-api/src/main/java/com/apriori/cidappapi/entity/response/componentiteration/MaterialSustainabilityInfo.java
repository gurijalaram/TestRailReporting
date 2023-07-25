package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MaterialSustainabilityInfo {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private Double hexBarCarbonEmissionsFactor;
    private Double rectangularBarCarbonEmissionsFactor;
    private Double roundBarCarbonEmissionsFactor;
    private Double roundTubeCarbonEmissionsFactor;
    private Double squareBarCarbonEmissionsFactor;
    private Double carbonEmissionsFactor;
}
