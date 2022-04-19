package com.apriori.acs.entity.response.acs.getartifactproperties;

import com.apriori.acs.entity.response.acs.getgcdmapping.ArtifactKeyItem;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.List;

@Data
public class PropertyValueMap {
    private Double angle;
    private ArtifactKeyItem artifactKey;
    private String artifactTypeName;
    private String cadKeyText;
    private Integer childArtifactCount;
    private Boolean convex;
    private Double edgeLength;
    private Integer minGcdVersion;
    private Array parentArtifact;
    private ArtifactKeyItem parentArtifactKey;
    private List<Object> relations;
    private Double roundRadius;
}
