package com.apriori.cidappapi.models.response.componentiteration;

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
public class MaterialStock {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Double costPerUnit;
    private String costUnits;
    private Double flangeThickness;
    private String description;
    private String formName;
    private Double hardness;
    private String hardnessSystem;
    private Double height;
    private Double insideDiameter;
    private Double length;
    private Double massPerMeter;
    private String name;
    private Double outsideDiameter;
    private Double wallThickness;
    private Double webThickness;
    private Boolean virtual;
    private Double width;
    private Double thickness;
    private MaterialSustainabilityInfo sustainabilityInfo;
}
