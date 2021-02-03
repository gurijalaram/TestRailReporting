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

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAltName1() {
        return altName1;
    }

    public void setAltName1(String altName1) {
        this.altName1 = altName1;
    }

    public String getAltName2() {
        return altName2;
    }

    public void setAltName2(String altName2) {
        this.altName2 = altName2;
    }

    public String getAltName3() {
        return altName3;
    }

    public void setAltName3(String altName3) {
        this.altName3 = altName3;
    }

    public String getAltName4() {
        return altName4;
    }

    public void setAltName4(String altName4) {
        this.altName4 = altName4;
    }

    public String getAltName5() {
        return altName5;
    }

    public void setAltName5(String altName5) {
        this.altName5 = altName5;
    }

    public String getCostUnits() {
        return costUnits;
    }

    public void setCostUnits(String costUnits) {
        this.costUnits = costUnits;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getDensity() {
        return density;
    }

    public void setDensity(Integer density) {
        this.density = density;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHardness() {
        return hardness;
    }

    public void setHardness(Integer hardness) {
        this.hardness = hardness;
    }

    public String getHardnessSystem() {
        return hardnessSystem;
    }

    public void setHardnessSystem(String hardnessSystem) {
        this.hardnessSystem = hardnessSystem;
    }

    public Double getMaterialCutCodeName() {
        return materialCutCodeName;
    }

    public void setMaterialCutCodeName(Double materialCutCodeName) {
        this.materialCutCodeName = materialCutCodeName;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public Integer getMillingSpeed() {
        return millingSpeed;
    }

    public void setMillingSpeed(Integer millingSpeed) {
        this.millingSpeed = millingSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScrapCostPercent() {
        return scrapCostPercent;
    }

    public void setScrapCostPercent(Double scrapCostPercent) {
        this.scrapCostPercent = scrapCostPercent;
    }

    public Integer getShearStrength() {
        return shearStrength;
    }

    public void setShearStrength(Integer shearStrength) {
        this.shearStrength = shearStrength;
    }
}
