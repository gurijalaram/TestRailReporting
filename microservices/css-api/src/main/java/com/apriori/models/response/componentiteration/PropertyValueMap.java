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
    private Double minStockLength;
    private String cadMaterialName;
}
