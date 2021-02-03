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
    public void setBlankBoxLength(Integer blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
    }

    @JsonProperty("blankBoxWidth")
    public Integer getBlankBoxWidth() {
        return blankBoxWidth;
    }

    @JsonProperty("blankBoxWidth")
    public void setBlankBoxWidth(Integer blankBoxWidth) {
        this.blankBoxWidth = blankBoxWidth;
    }

    @JsonProperty("cadConfiguration")
    public String getCadConfiguration() {
        return cadConfiguration;
    }

    @JsonProperty("cadConfiguration")
    public void setCadConfiguration(String cadConfiguration) {
        this.cadConfiguration = cadConfiguration;
    }

    @JsonProperty("cadKeyText")
    public String getCadKeyText() {
        return cadKeyText;
    }

    @JsonProperty("cadKeyText")
    public void setCadKeyText(String cadKeyText) {
        this.cadKeyText = cadKeyText;
    }

    @JsonProperty("cadMaterialName")
    public String getCadMaterialName() {
        return cadMaterialName;
    }

    @JsonProperty("cadMaterialName")
    public void setCadMaterialName(String cadMaterialName) {
        this.cadMaterialName = cadMaterialName;
    }

    @JsonProperty("cadVersion")
    public Integer getCadVersion() {
        return cadVersion;
    }

    @JsonProperty("cadVersion")
    public void setCadVersion(Integer cadVersion) {
        this.cadVersion = cadVersion;
    }

    @JsonProperty("childArtifactCount")
    public Integer getChildArtifactCount() {
        return childArtifactCount;
    }

    @JsonProperty("childArtifactCount")
    public void setChildArtifactCount(Integer childArtifactCount) {
        this.childArtifactCount = childArtifactCount;
    }

    @JsonProperty("childGcds")
    public String getChildGcds() {
        return childGcds;
    }

    @JsonProperty("childGcds")
    public void setChildGcds(String childGcds) {
        this.childGcds = childGcds;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public void setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
    }

    @JsonProperty("descriptor")
    public String getDescriptor() {
        return descriptor;
    }

    @JsonProperty("descriptor")
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @JsonProperty("distanceUnits")
    public Integer getDistanceUnits() {
        return distanceUnits;
    }

    @JsonProperty("distanceUnits")
    public void setDistanceUnits(Integer distanceUnits) {
        this.distanceUnits = distanceUnits;
    }

    @JsonProperty("engineType")
    public String getEngineType() {
        return engineType;
    }

    @JsonProperty("engineType")
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    @JsonProperty("fileFormat")
    public String getFileFormat() {
        return fileFormat;
    }

    @JsonProperty("fileFormat")
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("massUnitName")
    public String getMassUnitName() {
        return massUnitName;
    }

    @JsonProperty("massUnitName")
    public void setMassUnitName(String massUnitName) {
        this.massUnitName = massUnitName;
    }

    @JsonProperty("minGcdVersion")
    public Integer getMinGcdVersion() {
        return minGcdVersion;
    }

    @JsonProperty("minGcdVersion")
    public void setMinGcdVersion(Integer minGcdVersion) {
        this.minGcdVersion = minGcdVersion;
    }

    @JsonProperty("nonSolidSurfaceArea")
    public Integer getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    @JsonProperty("nonSolidSurfaceArea")
    public void setNonSolidSurfaceArea(Integer nonSolidSurfaceArea) {
        this.nonSolidSurfaceArea = nonSolidSurfaceArea;
    }

    @JsonProperty("partModelName")
    public String getPartModelName() {
        return partModelName;
    }

    @JsonProperty("partModelName")
    public void setPartModelName(String partModelName) {
        this.partModelName = partModelName;
    }

    @JsonProperty("surfaceArea")
    public Integer getSurfaceArea() {
        return surfaceArea;
    }

    @JsonProperty("surfaceArea")
    public void setSurfaceArea(Integer surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    @JsonProperty("temperatureUnitName")
    public String getTemperatureUnitName() {
        return temperatureUnitName;
    }

    @JsonProperty("temperatureUnitName")
    public void setTemperatureUnitName(String temperatureUnitName) {
        this.temperatureUnitName = temperatureUnitName;
    }

    @JsonProperty("thickness")
    public Integer getThickness() {
        return thickness;
    }

    @JsonProperty("thickness")
    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    @JsonProperty("timeUnitName")
    public String getTimeUnitName() {
        return timeUnitName;
    }

    @JsonProperty("timeUnitName")
    public void setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    @JsonProperty("volume")
    public Integer getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }
}
