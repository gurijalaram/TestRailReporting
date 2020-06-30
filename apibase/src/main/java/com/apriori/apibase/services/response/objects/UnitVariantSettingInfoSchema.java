package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitVariantSettingInfoSchema {

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

    public UnitVariantSettingInfoSchema setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public UnitVariantSettingInfoSchema setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getMetric() {
        return metric;
    }

    public UnitVariantSettingInfoSchema setMetric(Boolean metric) {
        this.metric = metric;
        return this;
    }

    public String getLength() {
        return length;
    }

    public UnitVariantSettingInfoSchema setLength(String length) {
        this.length = length;
        return this;
    }

    public String getMass() {
        return mass;
    }

    public UnitVariantSettingInfoSchema setMass(String mass) {
        this.mass = mass;
        return this;
    }

    public String getTime() {
        return time;
    }

    public UnitVariantSettingInfoSchema setTime(String time) {
        this.time = time;
        return this;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public UnitVariantSettingInfoSchema setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public Boolean getSystem() {
        return system;
    }

    public UnitVariantSettingInfoSchema setSystem(Boolean system) {
        this.system = system;
        return this;
    }

    public Boolean getCustom() {
        return custom;
    }

    public UnitVariantSettingInfoSchema setCustom(Boolean custom) {
        this.custom = custom;
        return this;
    }
}
