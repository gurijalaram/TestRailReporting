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
    private NumSurfaces numSurfaces;
    private NumInsertCores numInsertCores;
    private MinThickness minThickness;
    private ToolThickness toolThickness;
    private ThicknessStdDev thicknessStdDev;
    private AverageThickness averageThickness;
    private ToolMinThickness toolMinThickness;
    private MaxThickness maxThickness;
    private NumSideCores numSideCores;
    private AverageThicknessDev averageThicknessDev;
    private Min80Thickness min80Thickness;

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

    public NumSurfaces getNumSurfaces() {
        return numSurfaces;
    }

    public PropertyInfoMap setNumSurfaces(NumSurfaces numSurfaces) {
        this.numSurfaces = numSurfaces;
        return this;
    }

    public NumInsertCores getNumInsertCores() {
        return numInsertCores;
    }

    public PropertyInfoMap setNumInsertCores(NumInsertCores numInsertCores) {
        this.numInsertCores = numInsertCores;
        return this;
    }

    public MinThickness getMinThickness() {
        return minThickness;
    }

    public PropertyInfoMap setMinThickness(MinThickness minThickness) {
        this.minThickness = minThickness;
        return this;
    }

    public ToolThickness getToolThickness() {
        return toolThickness;
    }

    public PropertyInfoMap setToolThickness(ToolThickness toolThickness) {
        this.toolThickness = toolThickness;
        return this;
    }

    public ThicknessStdDev getThicknessStdDev() {
        return thicknessStdDev;
    }

    public PropertyInfoMap setThicknessStdDev(ThicknessStdDev thicknessStdDev) {
        this.thicknessStdDev = thicknessStdDev;
        return this;
    }

    public AverageThickness getAverageThickness() {
        return averageThickness;
    }

    public PropertyInfoMap setAverageThickness(AverageThickness averageThickness) {
        this.averageThickness = averageThickness;
        return this;
    }

    public ToolMinThickness getToolMinThickness() {
        return toolMinThickness;
    }

    public PropertyInfoMap setToolMinThickness(ToolMinThickness toolMinThickness) {
        this.toolMinThickness = toolMinThickness;
        return this;
    }

    public MaxThickness getMaxThickness() {
        return maxThickness;
    }

    public PropertyInfoMap setMaxThickness(MaxThickness maxThickness) {
        this.maxThickness = maxThickness;
        return this;
    }

    public NumSideCores getNumSideCores() {
        return numSideCores;
    }

    public PropertyInfoMap setNumSideCores(NumSideCores numSideCores) {
        this.numSideCores = numSideCores;
        return this;
    }

    public AverageThicknessDev getAverageThicknessDev() {
        return averageThicknessDev;
    }

    public PropertyInfoMap setAverageThicknessDev(AverageThicknessDev averageThicknessDev) {
        this.averageThicknessDev = averageThicknessDev;
        return this;
    }

    public Min80Thickness getMin80Thickness() {
        return min80Thickness;
    }

    public PropertyInfoMap setMin80Thickness(Min80Thickness min80Thickness) {
        this.min80Thickness = min80Thickness;
        return this;
    }

    class Min80Thickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Min80Thickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Min80Thickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class AverageThicknessDev {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public AverageThicknessDev setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public AverageThicknessDev setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class AverageThickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public AverageThickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public AverageThickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class NumSideCores {
        private String name;

        public String getName() {
            return name;
        }

        public NumSideCores setName(String name) {
            this.name = name;
            return this;
        }
    }

    class NonSolidSurfaceArea {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public NonSolidSurfaceArea setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public NonSolidSurfaceArea setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class NumSurfaces {
        private String name;

        public String getName() {
            return name;
        }

        public NumSurfaces setName(String name) {
            this.name = name;
            return this;
        }
    }

    class NumInsertCores {
        private String name;

        public String getName() {
            return name;
        }

        public NumInsertCores setName(String name) {
            this.name = name;
            return this;
        }
    }

    class MinThickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public MinThickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public MinThickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class MaxThickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public MaxThickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public MaxThickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class ArtifactTypeName {
        private String name;

        public String getName() {
            return name;
        }

        public ArtifactTypeName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class FileBaseName {
        private String name;

        public String getName() {
            return name;
        }

        public FileBaseName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class ToolThickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public ToolThickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public ToolThickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class BlankBoxWidth {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public BlankBoxWidth setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public BlankBoxWidth setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class TemperatureUnitName {
        private String name;

        public String getName() {
            return name;
        }

        public TemperatureUnitName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class PartModelName {
        private String name;

        public String getName() {
            return name;
        }

        public PartModelName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class ChildArtifactCount {
        private String name;

        public String getName() {
            return name;
        }

        public ChildArtifactCount setName(String name) {
            this.name = name;
            return this;
        }
    }

    class Width {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Width setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Width setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class Volume {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Volume setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Volume setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class DistanceUnits {
        private String name;

        public String getName() {
            return name;
        }

        public DistanceUnits setName(String name) {
            this.name = name;
            return this;
        }
    }

    class FileFormat {
        private String name;

        public String getName() {
            return name;
        }

        public FileFormat setName(String name) {
            this.name = name;
            return this;
        }
    }

    class CadMaterialName {
        private String name;

        public String getName() {
            return name;
        }

        public CadMaterialName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class CadVersion {
        private String name;

        public String getName() {
            return name;
        }

        public CadVersion setName(String name) {
            this.name = name;
            return this;
        }
    }

    class CadKeyText {
        private String name;

        public String getName() {
            return name;
        }

        public CadKeyText setName(String name) {
            this.name = name;
            return this;
        }
    }

    class CadConfiguration {
        private String name;

        public String getName() {
            return name;
        }

        public CadConfiguration setName(String name) {
            this.name = name;
            return this;
        }
    }

    class Descriptor {
        private String name;

        public String getName() {
            return name;
        }

        public Descriptor setName(String name) {
            this.name = name;
            return this;
        }
    }

    class BlankBoxLength {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public BlankBoxLength setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public BlankBoxLength setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class EngineType {
        private String name;

        public String getName() {
            return name;
        }

        public EngineType setName(String name) {
            this.name = name;
            return this;
        }
    }

    class Height {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Height setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Height setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class Length {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Length setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Length setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class MassUnitName {
        private String name;

        public String getName() {
            return name;
        }

        public MassUnitName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class MinGcdVersion {
        private String name;

        public String getName() {
            return name;
        }

        public MinGcdVersion setName(String name) {
            this.name = name;
            return this;
        }
    }

    class SurfaceArea {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public SurfaceArea setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public SurfaceArea setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class Thickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public Thickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public Thickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class ThicknessStdDev {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public ThicknessStdDev setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public ThicknessStdDev setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }

    class TimeUnitName {
        private String name;

        public String getName() {
            return name;
        }

        public TimeUnitName setName(String name) {
            this.name = name;
            return this;
        }
    }

    class ToolMinThickness {
        private String name;
        private String unitTypeName;

        public String getName() {
            return name;
        }

        public ToolMinThickness setName(String name) {
            this.name = name;
            return this;
        }

        public String getUnitTypeName() {
            return unitTypeName;
        }

        public ToolMinThickness setUnitTypeName(String unitTypeName) {
            this.unitTypeName = unitTypeName;
            return this;
        }
    }
}
