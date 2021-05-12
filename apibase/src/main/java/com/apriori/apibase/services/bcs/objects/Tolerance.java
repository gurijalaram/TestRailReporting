package com.apriori.apibase.services.bcs.objects;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "bcs/CisToleranceSchema.json")
public class Tolerance {
    private String name;
    private String unit;
    private Double value;

    public Double getValue() {
        return value;
    }

    public Tolerance setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tolerance setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnit() {
        return this.unit;
    }

    public Tolerance setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}
