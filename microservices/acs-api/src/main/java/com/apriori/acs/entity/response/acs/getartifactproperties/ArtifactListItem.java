package com.apriori.acs.entity.response.acs.getartifactproperties;

import com.apriori.acs.entity.response.acs.getgcdmapping.ArtifactKeyItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Array;

@Data
public class ArtifactListItem {
    @JsonProperty("@type")
    private String type;
    private String overrideStatus;
    private String requested;
    private VolumeFieldsItem volumeFields;
    private Array roughness;
    private ArtifactKeyItem artifactKey;
    private Array roughnessRz;
    private Array tolerance;
    private Array diamTolerance;
    private Array positionTolerance;
    private Array circularity;
    private Array concentricity;
    private Array cylindricity;
    private Array flatness;
    private Array perpendicularity;
    private Array profileOfSurface;
    private Array runout;
    private Array totalRunout;
    private Array straightness;
    private Array symmetry;
    private Array bendAngleTolerance;
    private Array minTolerance;
    private Double surfaceFinishingRate;
    private Double volumeRemovalRate;
    private Array parallelism;
    private String selected;
    private Double time;
    private StatusItem status;
    private String name;
    private ArtifactData artifactData;
}
