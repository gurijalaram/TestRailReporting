package com.apriori.cidapp.entity.response.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Material {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String altName1;
    private String altName2;
    private String altName3;
    private String altName4;
    private String altName5;
    private Double costPerUnit;
    private String costUnits;
    private String dataSource;
    private Double density;
    private String description;
    private Double hardness;
    private String hardnessSystem;
    private Double k;
    private Double materialCutCodeName;
    private String materialTypeName;
    private Double millingSpeed;
    private Double n;
    private String name;
    private Double possionRatio;
    private Double r;
    private Double scrapCostPercent;
    private Double shearStrength;
    private Double tensileYieldStrength;
    private Double ultimateTensileStrength;
    private Double youngModulus;

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

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public Material setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
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

    public Double getDensity() {
        return density;
    }

    public Material setDensity(Double density) {
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

    public Double getHardness() {
        return hardness;
    }

    public Material setHardness(Double hardness) {
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

    public Double getK() {
        return k;
    }

    public Material setK(Double k) {
        this.k = k;
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

    public Double getMillingSpeed() {
        return millingSpeed;
    }

    public Material setMillingSpeed(Double millingSpeed) {
        this.millingSpeed = millingSpeed;
        return this;
    }

    public Double getN() {
        return n;
    }

    public Material setN(Double n) {
        this.n = n;
        return this;
    }

    public String getName() {
        return name;
    }

    public Material setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPossionRatio() {
        return possionRatio;
    }

    public Material setPossionRatio(Double possionRatio) {
        this.possionRatio = possionRatio;
        return this;
    }

    public Double getR() {
        return r;
    }

    public Material setR(Double r) {
        this.r = r;
        return this;
    }

    public Double getScrapCostPercent() {
        return scrapCostPercent;
    }

    public Material setScrapCostPercent(Double scrapCostPercent) {
        this.scrapCostPercent = scrapCostPercent;
        return this;
    }

    public Double getShearStrength() {
        return shearStrength;
    }

    public Material setShearStrength(Double shearStrength) {
        this.shearStrength = shearStrength;
        return this;
    }

    public Double getTensileYieldStrength() {
        return tensileYieldStrength;
    }

    public Material setTensileYieldStrength(Double tensileYieldStrength) {
        this.tensileYieldStrength = tensileYieldStrength;
        return this;
    }

    public Double getUltimateTensileStrength() {
        return ultimateTensileStrength;
    }

    public Material setUltimateTensileStrength(Double ultimateTensileStrength) {
        this.ultimateTensileStrength = ultimateTensileStrength;
        return this;
    }

    public Double getYoungModulus() {
        return youngModulus;
    }

    public Material setYoungModulus(Double youngModulus) {
        this.youngModulus = youngModulus;
        return this;
    }
}
