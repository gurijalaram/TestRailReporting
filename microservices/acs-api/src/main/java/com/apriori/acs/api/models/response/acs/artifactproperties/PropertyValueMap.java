package com.apriori.acs.api.models.response.acs.artifactproperties;

import com.apriori.acs.api.models.response.acs.gcdmapping.ArtifactKeyItem;

import lombok.Data;

import java.util.List;

@Data
public class PropertyValueMap {
    private Boolean accessible;
    private Double angle;
    private ArtifactKeyItem artifactKey;
    private String artifactTypeName;
    private String cadKeyText;
    private Integer childArtifactCount;
    private String edgeTypeName;
    private Double height;
    private Boolean internal;
    private Boolean isPositionToleranceSpecified;
    private Boolean isToleranceSpecified;
    private Double length;
    private Double maxWallAngle;
    private Double minConcaveRadius;
    private Double minConvexRadius;
    private Integer minGcdVersion;
    private Double minWallAngle;
    private Object parentArtifact;
    private ArtifactKeyItem parentArtifactKey;
    private Double positionTolerance;
    private List<Object> relations;
    private Double roundRadius;
    private Boolean straight;
    private Double tolerance;
}
