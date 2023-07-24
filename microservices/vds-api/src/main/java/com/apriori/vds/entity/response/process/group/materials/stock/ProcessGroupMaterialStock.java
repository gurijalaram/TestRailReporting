package com.apriori.vds.entity.response.process.group.materials.stock;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.utils.http.enums.Schema;
import com.apriori.vds.entity.response.process.group.materials.SustainabilityInfo;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "ProcessGroupMaterialStockResponse.json")
@Data
@JsonRootName(value = "response")
public class ProcessGroupMaterialStock {
    private Double costPerUnit;
    private String costUnits;
    private String createdBy;
    private String deletedBy;
    private String description;
    private Double flangeThickness;
    private String formName;
    private Double hardness;
    private String hardnessSystem;
    private Double height;
    private String identity;
    private Double insideDiameter;
    private String isStructural;
    private Double length;
    private Double massPerMeter;
    private String name;
    private Double outsideDiameter;
    private String stockForm;
    private String structural;
    private String subTypeName;
    private Double thickness;
    private String updatedBy;
    private String virtual;
    private Double wallThickness;
    private Double webThickness;
    private Double width;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private SustainabilityInfo sustainabilityInfo;
}
