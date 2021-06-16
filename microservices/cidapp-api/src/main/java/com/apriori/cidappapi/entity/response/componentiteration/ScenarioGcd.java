package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioGcd {
    private String identity;
    private Double blankBoxLength;
    private Double blankBoxWidth;
    private String cadConfiguration;
    private Integer cadVersion;
    private Integer childArtifactCount;
    private String childGcds;
    private Integer distanceUnits;
    private String engineType;
    private String fileFormat;
    private Double height;
    private Double length;
    private String massUnitName;
    private Integer minGcdVersion;
    private Double nonSolidSurfaceArea;
    private String partModelName;
    private Double surfaceArea;
    private String temperatureUnitName;
    private Double thickness;
    private String timeUnitName;
    private Double volume;
    private Double width;

    public String getIdentity() {
        return identity;
    }

    public ScenarioGcd setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Double getBlankBoxLength() {
        return blankBoxLength;
    }

    public ScenarioGcd setBlankBoxLength(Double blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
        return this;
    }

    public Double getBlankBoxWidth() {
        return blankBoxWidth;
    }

    public ScenarioGcd setBlankBoxWidth(Double blankBoxWidth) {
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

    public Double getHeight() {
        return height;
    }

    public ScenarioGcd setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public ScenarioGcd setLength(Double length) {
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

    public Double getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    public ScenarioGcd setNonSolidSurfaceArea(Double nonSolidSurfaceArea) {
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

    public Double getSurfaceArea() {
        return surfaceArea;
    }

    public ScenarioGcd setSurfaceArea(Double surfaceArea) {
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

    public Double getThickness() {
        return thickness;
    }

    public ScenarioGcd setThickness(Double thickness) {
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

    public Double getVolume() {
        return volume;
    }

    public ScenarioGcd setVolume(Double volume) {
        this.volume = volume;
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public ScenarioGcd setWidth(Double width) {
        this.width = width;
        return this;
    }
}
