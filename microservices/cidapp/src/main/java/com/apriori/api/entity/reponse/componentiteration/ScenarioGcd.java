package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioGcd {
    private Integer blankBoxLength;
    private Integer blankBoxWidth;
    private String cadConfiguration;
    private String cadKeyText;
    private String cadMaterialName;
    private Integer cadVersion;
    private Integer childArtifactCount;
    private String childGcds;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String descriptor;
    private Integer distanceUnits;
    private String engineType;
    private String fileFormat;
    private Integer height;
    private String identity;
    private Integer length;
    private String massUnitName;
    private Integer minGcdVersion;
    private Integer nonSolidSurfaceArea;
    private String partModelName;
    private Integer surfaceArea;
    private String temperatureUnitName;
    private Integer thickness;
    private String timeUnitName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;
    private Integer volume;
    private Integer width;

    public Integer getBlankBoxLength() {
        return blankBoxLength;
    }

    public ScenarioGcd setBlankBoxLength(Integer blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
        return this;
    }

    public Integer getBlankBoxWidth() {
        return blankBoxWidth;
    }

    public ScenarioGcd setBlankBoxWidth(Integer blankBoxWidth) {
        this.blankBoxWidth = blankBoxWidth;
        return this;
    }

    public String getCadConfiguration() {
        return cadConfiguration;
    }

    public ScenarioGcd setCadConfiguration(String cadConfiguration) {
        this.cadConfiguration = cadConfiguration;
        return this;
    }

    public String getCadKeyText() {
        return cadKeyText;
    }

    public ScenarioGcd setCadKeyText(String cadKeyText) {
        this.cadKeyText = cadKeyText;
        return this;
    }

    public String getCadMaterialName() {
        return cadMaterialName;
    }

    public ScenarioGcd setCadMaterialName(String cadMaterialName) {
        this.cadMaterialName = cadMaterialName;
        return this;
    }

    public Integer getCadVersion() {
        return cadVersion;
    }

    public ScenarioGcd setCadVersion(Integer cadVersion) {
        this.cadVersion = cadVersion;
        return this;
    }

    public Integer getChildArtifactCount() {
        return childArtifactCount;
    }

    public ScenarioGcd setChildArtifactCount(Integer childArtifactCount) {
        this.childArtifactCount = childArtifactCount;
        return this;
    }

    public String getChildGcds() {
        return childGcds;
    }

    public ScenarioGcd setChildGcds(String childGcds) {
        this.childGcds = childGcds;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ScenarioGcd setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ScenarioGcd setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ScenarioGcd setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ScenarioGcd setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public ScenarioGcd setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public ScenarioGcd setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public ScenarioGcd setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public Integer getDistanceUnits() {
        return distanceUnits;
    }

    public ScenarioGcd setDistanceUnits(Integer distanceUnits) {
        this.distanceUnits = distanceUnits;
        return this;
    }

    public String getEngineType() {
        return engineType;
    }

    public ScenarioGcd setEngineType(String engineType) {
        this.engineType = engineType;
        return this;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public ScenarioGcd setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public ScenarioGcd setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ScenarioGcd setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public ScenarioGcd setLength(Integer length) {
        this.length = length;
        return this;
    }

    public String getMassUnitName() {
        return massUnitName;
    }

    public ScenarioGcd setMassUnitName(String massUnitName) {
        this.massUnitName = massUnitName;
        return this;
    }

    public Integer getMinGcdVersion() {
        return minGcdVersion;
    }

    public ScenarioGcd setMinGcdVersion(Integer minGcdVersion) {
        this.minGcdVersion = minGcdVersion;
        return this;
    }

    public Integer getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    public ScenarioGcd setNonSolidSurfaceArea(Integer nonSolidSurfaceArea) {
        this.nonSolidSurfaceArea = nonSolidSurfaceArea;
        return this;
    }

    public String getPartModelName() {
        return partModelName;
    }

    public ScenarioGcd setPartModelName(String partModelName) {
        this.partModelName = partModelName;
        return this;
    }

    public Integer getSurfaceArea() {
        return surfaceArea;
    }

    public ScenarioGcd setSurfaceArea(Integer surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    public String getTemperatureUnitName() {
        return temperatureUnitName;
    }

    public ScenarioGcd setTemperatureUnitName(String temperatureUnitName) {
        this.temperatureUnitName = temperatureUnitName;
        return this;
    }

    public Integer getThickness() {
        return thickness;
    }

    public ScenarioGcd setThickness(Integer thickness) {
        this.thickness = thickness;
        return this;
    }

    public String getTimeUnitName() {
        return timeUnitName;
    }

    public ScenarioGcd setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ScenarioGcd setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ScenarioGcd setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public ScenarioGcd setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public Integer getVolume() {
        return volume;
    }

    public ScenarioGcd setVolume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public ScenarioGcd setWidth(Integer width) {
        this.width = width;
        return this;
    }
}
