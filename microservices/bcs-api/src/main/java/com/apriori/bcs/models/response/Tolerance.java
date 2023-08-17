package com.apriori.bcs.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "ToleranceSchema.json")
public class Tolerance {
    private String name;
    private String unit;
    private Double value;
}
