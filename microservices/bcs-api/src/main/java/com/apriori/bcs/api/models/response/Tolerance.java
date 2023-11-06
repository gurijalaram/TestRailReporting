package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "ToleranceSchema.json")
public class Tolerance {
    private String name;
    private String unit;
    private Double value;
}
