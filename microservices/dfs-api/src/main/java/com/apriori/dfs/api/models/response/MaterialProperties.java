package com.apriori.dfs.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialProperties {
    private Double costPerKG;
    private Integer ultimateTensileStrength;
    private Double k;
    private Double shearStrength;
    private Integer youngModulus;
    private Integer tensileYieldStrength;
    private Double n;
    private Double possionRatio;
    private Double r;
    private Double scrapCostPercent;
    private String costUnits;
    private String sourceName;
    private Integer millingSpeed;
    private String dataSource;
}

