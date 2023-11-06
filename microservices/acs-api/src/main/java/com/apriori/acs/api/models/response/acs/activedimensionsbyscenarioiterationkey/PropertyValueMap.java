package com.apriori.acs.api.models.response.acs.activedimensionsbyscenarioiterationkey;

import lombok.Data;

@Data
public class PropertyValueMap {
    private String artifactTypeName;
    private double minWallThickness;
    private int minGcdVersion;
    private double surfaceArea;
    private double nonSolidSurfaceArea;
    private String engineType;
    private String cadVersion;
    private int numSurfaces;
    private String descriptor;
    private String cadConfiguration;
    private String fileBaseName;
    private int numInsertCores;
    private double height;
    private double minHoleDiameter;
    private String cadKeyText;
    private double minThickness;
    private double toolMinThickness;
    private double thicknessStdDev;
    private double averageThickness;
    private double length;
    private int childArtifactCount;
    private String timeUnitName;
    private double maxThickness;
    private String massUnitName;
    private int numSideCores;
    private int volume;
    private double averageThicknessDev;
    private String cadMaterialName;
    private double minRingWidth;
    private int distanceUnits;
    private double width;
    private String partModelName;
    private double min80Thickness;
    private String temperatureUnitName;
    private String fileFormat;
}
