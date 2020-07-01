package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitSystemSettingSchema {

    @JsonProperty("@type")
    private String type;

    @JsonProperty
    private String name;

    @JsonProperty
    private Boolean metric;

    @JsonProperty
    private String length;

    @JsonProperty
    private String mass;

    @JsonProperty
    private String time;

    @JsonProperty
    private Integer decimalPlaces;

    @JsonProperty
    private Boolean system;

    @JsonProperty
    private Boolean custom;

    public String getType() {
        return type;
    }

    public UnitSystemSettingSchema setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public UnitSystemSettingSchema setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getMetric() {
        return metric;
    }

    public UnitSystemSettingSchema setMetric(Boolean metric) {
        this.metric = metric;
        return this;
    }

    public String getLength() {
        return length;
    }

    public UnitSystemSettingSchema setLength(String length) {
        this.length = length;
        return this;
    }

    public String getMass() {
        return mass;
    }

    public UnitSystemSettingSchema setMass(String mass) {
        this.mass = mass;
        return this;
    }

    public String getTime() {
        return time;
    }

    public UnitSystemSettingSchema setTime(String time) {
        this.time = time;
        return this;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public UnitSystemSettingSchema setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public Boolean getSystem() {
        return system;
    }

    public UnitSystemSettingSchema setSystem(Boolean system) {
        this.system = system;
        return this;
    }

    public Boolean getCustom() {
        return custom;
    }

    public UnitSystemSettingSchema setCustom(Boolean custom) {
        this.custom = custom;
        return this;
    }
}
