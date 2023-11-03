package com.apriori.acs.api.models.response.acs.artifactproperties;

import lombok.Data;

@Data
public class PropertyInfoMap {
    private PropertyInfoMapItem accessible;
    private PropertyInfoMapLargerItem angle;
    private PropertyInfoMapItem artifactKey;
    private PropertyInfoMapItem artifactTypeName;
    private PropertyInfoMapItem cadKeyText;
    private PropertyInfoMapItem childArtifactCount;
    private PropertyInfoMapItem childArtifacts;
    private PropertyInfoMapItem edgeTypeName;
    private PropertyInfoMapLargerItem height;
    private PropertyInfoMapItem internal;
    private PropertyInfoMapItem isPositionToleranceSpecified;
    private PropertyInfoMapItem isToleranceSpecified;
    private PropertyInfoMapLargerItem length;
    private PropertyInfoMapLargerItem maxWallAngle;
    private PropertyInfoMapLargerItem minConcaveRadius;
    private PropertyInfoMapLargerItem minConvexRadius;
    private PropertyInfoMapItem minGcdVersion;
    private PropertyInfoMapLargerItem minWallAngle;
    private PropertyInfoMapItem parentArtifact;
    private PropertyInfoMapItem parentArtifactKey;
    private PropertyInfoMapLargerItem positionTolerance;
    private PropertyInfoMapItem relations;
    private PropertyInfoMapLargerItem roundRadius;
    private PropertyInfoMapItem setupGroupId;
    private PropertyInfoMapItem straight;
    private PropertyInfoMapLargerItem tolerance;
}
