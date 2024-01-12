package com.apriori.dfs.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(location = "MaterialStockSchema.json")
public class MaterialStocks {
    private String identity;
    private Double baseCostPerUnit;
    private String form;
    private Double hardness;
    private String hardnessSystem;
    private String name;
    private String materialIdentity;
    private MaterialStockProperties properties;
    private Double unitCost;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
}

