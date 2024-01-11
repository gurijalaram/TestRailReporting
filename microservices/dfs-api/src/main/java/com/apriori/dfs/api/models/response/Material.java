package com.apriori.dfs.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "MaterialSchema.json")
public class Material {
    private String identity;
    private Double baseCostPerUnit;
    private Double carbonEmissionsFactor;
    private String customerIdentity;
    private String cutCode;
    private String digitalFactoryIdentity;
    private Double density;
    private String description;
    private Double hardness;
    private String hardnessSystem;
    private MaterialIndustryStandardNames industryStandardNames;
    private String name;
    private String processGroupIdentity;
    private MaterialProperties properties;
    private String type;
    private Double unitCost;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
}

