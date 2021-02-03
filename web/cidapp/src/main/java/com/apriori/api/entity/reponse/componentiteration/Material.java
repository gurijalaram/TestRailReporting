package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

public class Material {

    @JsonProperty("identity")
    private String identity;
    @JsonProperty("createdAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("altName1")
    private String altName1;
    @JsonProperty("altName2")
    private String altName2;
    @JsonProperty("altName3")
    private String altName3;
    @JsonProperty("altName4")
    private String altName4;
    @JsonProperty("altName5")
    private String altName5;
    @JsonProperty("costUnits")
    private String costUnits;
    @JsonProperty("dataSource")
    private String dataSource;
    @JsonProperty("density")
    private Integer density;
    @JsonProperty("description")
    private String description;
    @JsonProperty("hardness")
    private Integer hardness;
    @JsonProperty("hardnessSystem")
    private String hardnessSystem;
    @JsonProperty("materialCutCodeName")
    private Double materialCutCodeName;
    @JsonProperty("materialTypeName")
    private String materialTypeName;
    @JsonProperty("millingSpeed")
    private Integer millingSpeed;
    @JsonProperty("name")
    private String name;
    @JsonProperty("scrapCostPercent")
    private Double scrapCostPercent;
    @JsonProperty("shearStrength")
    private Integer shearStrength;

    public String getIdentity() {
        return identity;
    }

    public Material setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Material setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Material setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getAltName1() {
        return altName1;
    }

    public Material setAltName1(String altName1) {
        this.altName1 = altName1;
        return this;
    }

    public String getAltName2() {
        return altName2;
    }

    public Material setAltName2(String altName2) {
        this.altName2 = altName2;
        return this;
    }

    public String getAltName3() {
        return altName3;
    }

    public Material setAltName3(String altName3) {
        this.altName3 = altName3;
        return this;
    }

    public String getAltName4() {
        return altName4;
    }

    public Material setAltName4(String altName4) {
        this.altName4 = altName4;
        return this;
    }

    public String getAltName5() {
        return altName5;
    }

    public Material setAltName5(String altName5) {
        this.altName5 = altName5;
        return this;
    }

    public String getCostUnits() {
        return costUnits;
    }

    public Material setCostUnits(String costUnits) {
        this.costUnits = costUnits;
        return this;
    }

    public String getDataSource() {
        return dataSource;
    }

    public Material setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public Integer getDensity() {
        return density;
    }

    public Material setDensity(Integer density) {
        this.density = density;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Material setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getHardness() {
        return hardness;
    }

    public Material setHardness(Integer hardness) {
        this.hardness = hardness;
        return this;
    }

    public String getHardnessSystem() {
        return hardnessSystem;
    }

    public Material setHardnessSystem(String hardnessSystem) {
        this.hardnessSystem = hardnessSystem;
        return this;
    }

    public Double getMaterialCutCodeName() {
        return materialCutCodeName;
    }

    public Material setMaterialCutCodeName(Double materialCutCodeName) {
        this.materialCutCodeName = materialCutCodeName;
        return this;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public Material setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
        return this;
    }

    public Integer getMillingSpeed() {
        return millingSpeed;
    }

    public Material setMillingSpeed(Integer millingSpeed) {
        this.millingSpeed = millingSpeed;
        return this;
    }

    public String getName() {
        return name;
    }

    public Material setName(String name) {
        this.name = name;
        return this;
    }

    public Double getScrapCostPercent() {
        return scrapCostPercent;
    }

    public Material setScrapCostPercent(Double scrapCostPercent) {
        this.scrapCostPercent = scrapCostPercent;
        return this;
    }

    public Integer getShearStrength() {
        return shearStrength;
    }

    public Material setShearStrength(Integer shearStrength) {
        this.shearStrength = shearStrength;
        return this;
    }
}
