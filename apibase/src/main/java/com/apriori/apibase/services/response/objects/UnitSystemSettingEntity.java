package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitSystemSettingEntity {

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

    public UnitSystemSettingEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public UnitSystemSettingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getMetric() {
        return metric;
    }

    public UnitSystemSettingEntity setMetric(Boolean metric) {
        this.metric = metric;
        return this;
    }

    public String getLength() {
        return length;
    }

    public UnitSystemSettingEntity setLength(String length) {
        this.length = length;
        return this;
    }

    public String getMass() {
        return mass;
    }

    public UnitSystemSettingEntity setMass(String mass) {
        this.mass = mass;
        return this;
    }

    public String getTime() {
        return time;
    }

    public UnitSystemSettingEntity setTime(String time) {
        this.time = time;
        return this;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public UnitSystemSettingEntity setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public Boolean getSystem() {
        return system;
    }

    public UnitSystemSettingEntity setSystem(Boolean system) {
        this.system = system;
        return this;
    }

    public Boolean getCustom() {
        return custom;
    }

    public UnitSystemSettingEntity setCustom(Boolean custom) {
        this.custom = custom;
        return this;
    }
}
