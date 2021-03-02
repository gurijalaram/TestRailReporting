package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyInfoMap {
    private ArtifactTypeName artifactTypeName;
    private MinGcdVersion minGcdVersion;
    private SurfaceArea surfaceArea;
    private NonSolidSurfaceArea nonSolidSurfaceArea;
    private Thickness thickness;
    private BlankBoxLength blankBoxLength;
    private EngineType engineType;
    private CadVersion cadVersion;
    private Descriptor descriptor;
    private CadConfiguration cadConfiguration;
    private FileBaseName fileBaseName;
    private Height height;
    private CadKeyText cadKeyText;
    private BlankBoxWidth blankBoxWidth;
    private Length length;
    private ChildArtifactCount childArtifactCount;
    private TimeUnitName timeUnitName;
    private MassUnitName massUnitName;
    private Volume volume;
    private CadMaterialName cadMaterialName;
    private DistanceUnits distanceUnits;
    private Width width;
    private PartModelName partModelName;
    private TemperatureUnitName temperatureUnitName;
    private FileFormat fileFormat;

    public ArtifactTypeName getArtifactTypeName() {
        return artifactTypeName;
    }

    public PropertyInfoMap setArtifactTypeName(ArtifactTypeName artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
        return this;
    }

    public MinGcdVersion getMinGcdVersion() {
        return minGcdVersion;
    }

    public PropertyInfoMap setMinGcdVersion(MinGcdVersion minGcdVersion) {
        this.minGcdVersion = minGcdVersion;
        return this;
    }

    public SurfaceArea getSurfaceArea() {
        return surfaceArea;
    }

    public PropertyInfoMap setSurfaceArea(SurfaceArea surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    public NonSolidSurfaceArea getNonSolidSurfaceArea() {
        return nonSolidSurfaceArea;
    }

    public PropertyInfoMap setNonSolidSurfaceArea(NonSolidSurfaceArea nonSolidSurfaceArea) {
        this.nonSolidSurfaceArea = nonSolidSurfaceArea;
        return this;
    }

    public Thickness getThickness() {
        return thickness;
    }

    public PropertyInfoMap setThickness(Thickness thickness) {
        this.thickness = thickness;
        return this;
    }

    public BlankBoxLength getBlankBoxLength() {
        return blankBoxLength;
    }

    public PropertyInfoMap setBlankBoxLength(BlankBoxLength blankBoxLength) {
        this.blankBoxLength = blankBoxLength;
        return this;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public PropertyInfoMap setEngineType(EngineType engineType) {
        this.engineType = engineType;
        return this;
    }

    public CadVersion getCadVersion() {
        return cadVersion;
    }

    public PropertyInfoMap setCadVersion(CadVersion cadVersion) {
        this.cadVersion = cadVersion;
        return this;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public PropertyInfoMap setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public CadConfiguration getCadConfiguration() {
        return cadConfiguration;
    }

    public PropertyInfoMap setCadConfiguration(CadConfiguration cadConfiguration) {
        this.cadConfiguration = cadConfiguration;
        return this;
    }

    public FileBaseName getFileBaseName() {
        return fileBaseName;
    }

    public PropertyInfoMap setFileBaseName(FileBaseName fileBaseName) {
        this.fileBaseName = fileBaseName;
        return this;
    }

    public Height getHeight() {
        return height;
    }

    public PropertyInfoMap setHeight(Height height) {
        this.height = height;
        return this;
    }

    public CadKeyText getCadKeyText() {
        return cadKeyText;
    }

    public PropertyInfoMap setCadKeyText(CadKeyText cadKeyText) {
        this.cadKeyText = cadKeyText;
        return this;
    }

    public BlankBoxWidth getBlankBoxWidth() {
        return blankBoxWidth;
    }

    public PropertyInfoMap setBlankBoxWidth(BlankBoxWidth blankBoxWidth) {
        this.blankBoxWidth = blankBoxWidth;
        return this;
    }

    public Length getLength() {
        return length;
    }

    public PropertyInfoMap setLength(Length length) {
        this.length = length;
        return this;
    }

    public ChildArtifactCount getChildArtifactCount() {
        return childArtifactCount;
    }

    public PropertyInfoMap setChildArtifactCount(ChildArtifactCount childArtifactCount) {
        this.childArtifactCount = childArtifactCount;
        return this;
    }

    public TimeUnitName getTimeUnitName() {
        return timeUnitName;
    }

    public PropertyInfoMap setTimeUnitName(TimeUnitName timeUnitName) {
        this.timeUnitName = timeUnitName;
        return this;
    }

    public MassUnitName getMassUnitName() {
        return massUnitName;
    }

    public PropertyInfoMap setMassUnitName(MassUnitName massUnitName) {
        this.massUnitName = massUnitName;
        return this;
    }

    public Volume getVolume() {
        return volume;
    }

    public PropertyInfoMap setVolume(Volume volume) {
        this.volume = volume;
        return this;
    }

    public CadMaterialName getCadMaterialName() {
        return cadMaterialName;
    }

    public PropertyInfoMap setCadMaterialName(CadMaterialName cadMaterialName) {
        this.cadMaterialName = cadMaterialName;
        return this;
    }

    public DistanceUnits getDistanceUnits() {
        return distanceUnits;
    }

    public PropertyInfoMap setDistanceUnits(DistanceUnits distanceUnits) {
        this.distanceUnits = distanceUnits;
        return this;
    }

    public Width getWidth() {
        return width;
    }

    public PropertyInfoMap setWidth(Width width) {
        this.width = width;
        return this;
    }

    public PartModelName getPartModelName() {
        return partModelName;
    }

    public PropertyInfoMap setPartModelName(PartModelName partModelName) {
        this.partModelName = partModelName;
        return this;
    }

    public TemperatureUnitName getTemperatureUnitName() {
        return temperatureUnitName;
    }

    public PropertyInfoMap setTemperatureUnitName(TemperatureUnitName temperatureUnitName) {
        this.temperatureUnitName = temperatureUnitName;
        return this;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public PropertyInfoMap setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }
}
