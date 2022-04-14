package com.apriori.acs.entity.response.acs.getartifactproperties;

import lombok.Data;

@Data
public class PropertyInfoMap {
    private PropertyInfoMapLargerItem angle;
    private PropertyInfoMapItem artifactKey;
    private PropertyInfoMapItem artifactTypeName;
    private PropertyInfoMapItem cadKeyText;
    private PropertyInfoMapItem childArtifactCount;
    private PropertyInfoMapItem childArtifacts;
    private PropertyInfoMapItem convex;
    private PropertyInfoMapItem edgeLength;
    private PropertyInfoMapItem minGcdVersion;
    private PropertyInfoMapItem parentArtifact;
    private PropertyInfoMapItem parentArtifactKey;
    private PropertyInfoMapItem relations;
    private PropertyInfoMapItem roundRadius;
    private PropertyInfoMapItem setupGroupId;
}
