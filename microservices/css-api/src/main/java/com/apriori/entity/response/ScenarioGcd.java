package com.apriori.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioGcd {
    private String identity;
    private String massUnitName;
    private String timeUnitName;
    private String temperatureUnitName;
    private String partModelName;
    private Double blankBoxLength;
    private Double blankBoxWidth;
    private Double thickness;
    private Double volume;
    private Double height;
    private Double length;
    private Double width;
    private Double surfaceArea;
    private Double nonSolidSurfaceArea;
    private Integer cadVersion;
    private Integer minGcdVersion;
    private Integer distanceUnits;
    private Integer childArtifactCount;
    private String childGcds;
    private String engineType;
    private String fileFormat;
}
