package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Min80Thickness {
        private String name;
        private String unitTypeName;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class AverageThicknessDev {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class AverageThickness {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class NumSideCores {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class NonSolidSurfaceArea {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class NumSurfaces {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class NumInsertCores {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class MinThickness {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class MaxThickness {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ArtifactTypeName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class FileBaseName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ToolThickness {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class BlankBoxWidth {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class TemperatureUnitName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class PartModelName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ChildArtifactCount {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Width {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Volume {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class DistanceUnits {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class FileFormat {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class CadMaterialName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class CadVersion {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class CadKeyText {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class CadConfiguration {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Descriptor {
        private String name;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class BlankBoxLength {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class EngineType {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Height {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Length {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class MassUnitName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class MinGcdVersion {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class SurfaceArea {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Thickness {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ThicknessStdDev {
        private String name;
        private String unitTypeName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class TimeUnitName {
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ToolMinThickness {
        private String name;
        private String unitTypeName;
    }
}
