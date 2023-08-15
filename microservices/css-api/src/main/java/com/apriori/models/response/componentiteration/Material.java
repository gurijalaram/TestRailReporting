package com.apriori.models.response.componentiteration;

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
public class Material {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String altName1;
    private String altName2;
    private String altName3;
    private String altName4;
    private String altName5;
    private Double costPerUnit;
    private String costUnits;
    private String dataSource;
    private Double density;
    private String description;
    private Double hardness;
    private String hardnessSystem;
    private Double k;
    private String materialCutCodeName;
    private String materialTypeName;
    private Double millingSpeed;
    private Double n;
    private String name;
    private Double possionRatio;
    private Double r;
    private Double scrapCostPercent;
    private Double shearStrength;
    private Double tensileYieldStrength;
    private Double ultimateTensileStrength;
    private Double youngModulus;
    private Double hexBarCostPerUnit;
    private String hexBarCostUnits;
    private Double plateCostPerUnit;
    private String plateCostUnits;
    private String rectangularBarCostUnits;
    private Double rectangularBarCostPerUnit;
    private Double roundBarCostPerUnit;
    private String roundBarCostUnits;
    private Double roundTubeCostPerUnit;
    private String roundTubeCostUnits;
    private Double squareBarCostPerUnit;
    private String squareBarCostUnits;
    private Double turningSpeed;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
}
