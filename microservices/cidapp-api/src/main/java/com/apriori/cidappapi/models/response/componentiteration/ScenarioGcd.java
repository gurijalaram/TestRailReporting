package com.apriori.cidappapi.models.response.componentiteration;

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
public class ScenarioGcd {
    private String identity;
    private Double blankBoxLength;
    private Double blankBoxWidth;
    private String cadConfiguration;
    private Integer cadVersion;
    private Integer childArtifactCount;
    private String childGcds;
    private String descriptor;
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
    private String cadMaterialName;
}
