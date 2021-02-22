package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ScenarioGcd {
    @JsonProperty("blankBoxLength")
    private Integer blankBoxLength;
    @JsonProperty("blankBoxWidth")
    private Integer blankBoxWidth;
    @JsonProperty("cadConfiguration")
    private String cadConfiguration;
    @JsonProperty("cadKeyText")
    private String cadKeyText;
    @JsonProperty("cadMaterialName")
    private String cadMaterialName;
    @JsonProperty("cadVersion")
    private Integer cadVersion;
    @JsonProperty("childArtifactCount")
    private Integer childArtifactCount;
    @JsonProperty("childGcds")
    private String childGcds;
    @JsonProperty("createdAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdByName")
    private String createdByName;
    @JsonProperty("deletedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    @JsonProperty("deletedBy")
    private String deletedBy;
    @JsonProperty("deletedByName")
    private String deletedByName;
    @JsonProperty("descriptor")
    private String descriptor;
    @JsonProperty("distanceUnits")
    private Integer distanceUnits;
    @JsonProperty("engineType")
    private String engineType;
    @JsonProperty("fileFormat")
    private String fileFormat;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("massUnitName")
    private String massUnitName;
    @JsonProperty("minGcdVersion")
    private Integer minGcdVersion;
    @JsonProperty("nonSolidSurfaceArea")
    private Integer nonSolidSurfaceArea;
    @JsonProperty("partModelName")
    private String partModelName;
    @JsonProperty("surfaceArea")
    private Integer surfaceArea;
    @JsonProperty("temperatureUnitName")
    private String temperatureUnitName;
    @JsonProperty("thickness")
    private Integer thickness;
    @JsonProperty("timeUnitName")
    private String timeUnitName;
    @JsonProperty("updatedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("updatedByName")
    private String updatedByName;
    @JsonProperty("volume")
    private Integer volume;
    @JsonProperty("width")
    private Integer width;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("blankBoxLength")
    public Integer getBlankBoxLength() {
        return blankBoxLength;
    }

    @JsonProperty("blankBoxLength")
    public ScenarioGcd setBlankBoxLength(Integer blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
        return this;
    }

    @JsonProperty("blankBoxWidth")
    public Integer getBlankBoxWidth() {
        return blankBoxWidth;
    }

    @JsonProperty("blankBoxWidth")
    public ScenarioGcd setBlankBoxWidth(Integer blankBoxWidth) {
        this.blankBoxWidth = blankBoxWidth;
        return this;
    }

    @JsonProperty("cadConfiguration")
    public String getCadConfiguration() {
        return cadConfiguration;
    }

    @JsonProperty("cadConfiguration")
    public ScenarioGcd setCadConfiguration(String cadConfiguration) {
        this.cadConfiguration = cadConfiguration;
        return this;
    }

    @JsonProperty("cadKeyText")
    public String getCadKeyText() {
        return cadKeyText;
    }

    @JsonProperty("cadKeyText")
    public ScenarioGcd setCadKeyText(String cadKeyText) {
        this.cadKeyText = cadKeyText;
        return this;
    }

    @JsonProperty("cadMaterialName")
    public String getCadMaterialName() {
        return cadMaterialName;
    }

    @JsonProperty("cadMaterialName")
    public ScenarioGcd setCadMaterialName(String cadMaterialName) {
        this.cadMaterialName = cadMaterialName;
        return this;
    }

    @JsonProperty("cadVersion")
    public Integer getCadVersion() {
        return cadVersion;
    }

    @JsonProperty("cadVersion")
    public ScenarioGcd setCadVersion(Integer cadVersion) {
        this.cadVersion = cadVersion;
        return this;
    }

    @JsonProperty("childArtifactCount")
    public Integer getChildArtifactCount() {
        return childArtifactCount;
    }

    @JsonProperty("childArtifactCount")
    public ScenarioGcd setChildArtifactCount(Integer childArtifactCount) {
        this.childArtifactCount = childArtifactCount;
        return this;
    }

    @JsonProperty("childGcds")
    public String getChildGcds() {
        return childGcds;
    }

    @JsonProperty("childGcds")
    public ScenarioGcd setChildGcds(String childGcds) {
        this.childGcds = childGcds;
        return this;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public ScenarioGcd setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public ScenarioGcd setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public ScenarioGcd setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public ScenarioGcd setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public ScenarioGcd setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public ScenarioGcd setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    @JsonProperty("descriptor")
    public String getDescriptor() {
        return descriptor;
    }

    @JsonProperty("descriptor")
    public ScenarioGcd setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    @JsonProperty("distanceUnits")
    public Integer getDistanceUnits() {
        return distanceUnits;
    }

    @JsonProperty("distanceUnits")
    public ScenarioGcd setDistanceUnits(Integer distanceUnits) {
        this.distanceUnits = distanceUnits;
        return this;
    }

    @JsonProperty("engineType")
    public String getEngineType() {
        return engineType;
    }

    @JsonProperty("engineType")
    public ScenarioGcd setEngineType(String engineType) {
        this.engineType = engineType;
        return this;
    }

    @JsonProperty("fileFormat")
    public String getFileFormat() {
        return fileFormat;
    }

    @JsonProperty("fileFormat")
    public ScenarioGcd setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public ScenarioGcd setHeight(Integer height) {
        this.height = height;
        return this;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public ScenarioGcd setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public ScenarioGcd setLength(Integer length) {
        this.length = length;
        return this;
    }

    @JsonProperty("massUnitName")
    public String getMassUnitName() {
        return massUnitName;
    }

    @JsonProperty("massUnitName")
    public ScenarioGcd setMassUnitName(String massUnitName) {
        this.massUnitName = massUnitName;
        return this;
    }

    @JsonProperty("minGcdVersion")
    public Integer getMinGcdVersion() {
        return minGcdVersion;
    }

    @JsonProperty("minGcdVersion")
    public ScenarioGcd setMinGcdVersion(Integer minGcdVersion) {
        this.minGcdVersion = minGcdVersion;
        return this;
    }

    @JsonProperty("nonSolidSurfaceArea")
    public Integer getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    @JsonProperty("nonSolidSurfaceArea")
    public ScenarioGcd setNonSolidSurfaceArea(Integer nonSolidSurfaceArea) {
        this.nonSolidSurfaceArea = nonSolidSurfaceArea;
        return this;
    }

    @JsonProperty("partModelName")
    public String getPartModelName() {
        return partModelName;
    }

    @JsonProperty("partModelName")
    public ScenarioGcd setPartModelName(String partModelName) {
        this.partModelName = partModelName;
        return this;
    }

    @JsonProperty("surfaceArea")
    public Integer getSurfaceArea() {
        return surfaceArea;
    }

    @JsonProperty("surfaceArea")
    public ScenarioGcd setSurfaceArea(Integer surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    @JsonProperty("temperatureUnitName")
    public String getTemperatureUnitName() {
        return temperatureUnitName;
    }

    @JsonProperty("temperatureUnitName")
    public ScenarioGcd setTemperatureUnitName(String temperatureUnitName) {
        this.temperatureUnitName = temperatureUnitName;
        return this;
    }

    @JsonProperty("thickness")
    public Integer getThickness() {
        return thickness;
    }

    @JsonProperty("thickness")
    public ScenarioGcd setThickness(Integer thickness) {
        this.thickness = thickness;
        return this;
    }

    @JsonProperty("timeUnitName")
    public String getTimeUnitName() {
        return timeUnitName;
    }

    @JsonProperty("timeUnitName")
    public ScenarioGcd setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
        return this;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public ScenarioGcd setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public ScenarioGcd setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public ScenarioGcd setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    @JsonProperty("volume")
    public Integer getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public ScenarioGcd setVolume(Integer volume) {
        this.volume = volume;
        return this;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public ScenarioGcd setWidth(Integer width) {
        this.width = width;
        return this;
    }
}
