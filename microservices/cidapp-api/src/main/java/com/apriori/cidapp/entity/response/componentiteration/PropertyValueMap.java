package com.apriori.cidapp.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyValueMap {
    private String artifactTypeName;
    private Integer minGcdVersion;
    private Double surfaceArea;
    private Double nonSolidSurfaceArea;
    private Double thickness;
    private Double blankBoxLength;
    private String engineType;
    private String cadVersion;
    private Double numSurfaces;
    private String cadConfiguration;
    private String fileBaseName;
    private Double numInsertCores;
    private Double height;
    private Double blankBoxWidth;
    private Double length;
    private Integer childArtifactCount;
    private String timeUnitName;
    private String massUnitName;
    private Double volume;
    private Integer distanceUnits;
    private Double width;
    private String partModelName;
    private String temperatureUnitName;
    private String fileFormat;
    private Double minThickness;
    private Double toolMinThickness;
    private Double thicknessStdDev;
    private Double averageThickness;
    private Double maxThickness;
    private Double numSideCores;
    private Double averageThicknessDev;
    private Double min80Thickness;

    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    public PropertyValueMap setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
        return this;
    }

    public Integer getMinGcdVersion() {
        return minGcdVersion;
    }

    public PropertyValueMap setMinGcdVersion(Integer minGcdVersion) {
        this.minGcdVersion = minGcdVersion;
        return this;
    }

    public Double getSurfaceArea() {
        return surfaceArea;
    }

    public PropertyValueMap setSurfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    public Double getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    public PropertyValueMap setNonSolidSurfaceArea(Double nonSolidSurfaceArea) {
        this.nonSolidSurfaceArea = nonSolidSurfaceArea;
        return this;
    }

    public Double getThickness() {
        return thickness;
    }

    public PropertyValueMap setThickness(Double thickness) {
        this.thickness = thickness;
        return this;
    }

    public Double getBlankBoxLength() {
        return blankBoxLength;
    }

    public PropertyValueMap setBlankBoxLength(Double blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
        return this;
    }

    public String getEngineType() {
        return engineType;
    }

    public PropertyValueMap setEngineType(String engineType) {
        this.engineType = engineType;
        return this;
    }

    public String getCadVersion() {
        return cadVersion;
    }

    public PropertyValueMap setCadVersion(String cadVersion) {
        this.cadVersion = cadVersion;
        return this;
    }

    public Double getNumSurfaces() {
        return numSurfaces;
    }

    public PropertyValueMap setNumSurfaces(Double numSurfaces) {
        this.numSurfaces = numSurfaces;
        return this;
    }

    public String getCadConfiguration() {
        return cadConfiguration;
    }

    public PropertyValueMap setCadConfiguration(String cadConfiguration) {
        this.cadConfiguration = cadConfiguration;
        return this;
    }

    public String getFileBaseName() {
        return fileBaseName;
    }

    public PropertyValueMap setFileBaseName(String fileBaseName) {
        this.fileBaseName = fileBaseName;
        return this;
    }

    public Double getNumInsertCores() {
        return numInsertCores;
    }

    public PropertyValueMap setNumInsertCores(Double numInsertCores) {
        this.numInsertCores = numInsertCores;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public PropertyValueMap setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getBlankBoxWidth() {
        return blankBoxWidth;
    }

    public PropertyValueMap setBlankBoxWidth(Double blankBoxWidth) {
        this.blankBoxWidth = blankBoxWidth;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public PropertyValueMap setLength(Double length) {
        this.length = length;
        return this;
    }

    public Integer getChildArtifactCount() {
        return childArtifactCount;
    }

    public PropertyValueMap setChildArtifactCount(Integer childArtifactCount) {
        this.childArtifactCount = childArtifactCount;
        return this;
    }

    public String getTimeUnitName() {
        return timeUnitName;
    }

    public PropertyValueMap setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
        return this;
    }

    public String getMassUnitName() {
        return massUnitName;
    }

    public PropertyValueMap setMassUnitName(String massUnitName) {
        this.massUnitName = massUnitName;
        return this;
    }

    public Double getVolume() {
        return volume;
    }

    public PropertyValueMap setVolume(Double volume) {
        this.volume = volume;
        return this;
    }

    public Integer getDistanceUnits() {
        return distanceUnits;
    }

    public PropertyValueMap setDistanceUnits(Integer distanceUnits) {
        this.distanceUnits = distanceUnits;
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public PropertyValueMap setWidth(Double width) {
        this.width = width;
        return this;
    }

    public String getPartModelName() {
        return partModelName;
    }

    public PropertyValueMap setPartModelName(String partModelName) {
        this.partModelName = partModelName;
        return this;
    }

    public String getTemperatureUnitName() {
        return temperatureUnitName;
    }

    public PropertyValueMap setTemperatureUnitName(String temperatureUnitName) {
        this.temperatureUnitName = temperatureUnitName;
        return this;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public PropertyValueMap setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    public Double getMinThickness() {
        return minThickness;
    }

    public PropertyValueMap setMinThickness(Double minThickness) {
        this.minThickness = minThickness;
        return this;
    }

    public Double getToolMinThickness() {
        return toolMinThickness;
    }

    public PropertyValueMap setToolMinThickness(Double toolMinThickness) {
        this.toolMinThickness = toolMinThickness;
        return this;
    }

    public Double getThicknessStdDev() {
        return thicknessStdDev;
    }

    public PropertyValueMap setThicknessStdDev(Double thicknessStdDev) {
        this.thicknessStdDev = thicknessStdDev;
        return this;
    }

    public Double getAverageThickness() {
        return averageThickness;
    }

    public PropertyValueMap setAverageThickness(Double averageThickness) {
        this.averageThickness = averageThickness;
        return this;
    }

    public Double getMaxThickness() {
        return maxThickness;
    }

    public PropertyValueMap setMaxThickness(Double maxThickness) {
        this.maxThickness = maxThickness;
        return this;
    }

    public Double getNumSideCores() {
        return numSideCores;
    }

    public PropertyValueMap setNumSideCores(Double numSideCores) {
        this.numSideCores = numSideCores;
        return this;
    }

    public Double getAverageThicknessDev() {
        return averageThicknessDev;
    }

    public PropertyValueMap setAverageThicknessDev(Double averageThicknessDev) {
        this.averageThicknessDev = averageThicknessDev;
        return this;
    }

    public Double getMin80Thickness() {
        return min80Thickness;
    }

    public PropertyValueMap setMin80Thickness(Double min80Thickness) {
        this.min80Thickness = min80Thickness;
        return this;
    }
}
