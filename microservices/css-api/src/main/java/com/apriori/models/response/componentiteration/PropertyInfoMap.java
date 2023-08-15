package com.apriori.models.response.componentiteration;

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
    private MinStockLength minStockLength;
}
