package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "ToleranceSchema.json")
public class Tolerance {
    private String name;
    private String unit;
    private Double value;
}
