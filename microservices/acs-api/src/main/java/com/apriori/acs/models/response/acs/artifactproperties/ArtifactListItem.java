package com.apriori.acs.models.response.acs.artifactproperties;

import com.apriori.acs.models.response.acs.gcdmapping.ArtifactKeyItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArtifactListItem {
    @JsonProperty("@type")
    private String type;
    private String overrideStatus;
    private String requested;
    private VolumeFieldsItem volumeFields;
    private Object circularity;
    private Object bendAngleTolerance;
    private Object parallelism;
    private Object roughness;
    private Object roughnessRz;
    private Object tolerance;
    private Object diamTolerance;
    private Object positionTolerance;
    private Object concentricity;
    private Object cylindricity;
    private Object perpendicularity;
    private Object runout;
    private Object totalRunout;
    private Object straightness;
    private Object symmetry;
    private Object profileOfSurface;
    private Object flatness;
    private Object minTolerance;
    private Double surfaceFinishingRate;
    private Double volumeRemovalRate;
    private String selected;
    private ArtifactKeyItem artifactKey;
    private Double time;
    private StatusItem status;
    private String name;
    private ArtifactData artifactData;
}
