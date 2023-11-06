package com.apriori.acs.api.models.response.acs.artifacttableinfo;

import lombok.Data;

@Data
public class ColumnNameIndexItem {
    private Integer requested;
    private Integer selected;
    private Integer time;
    private Integer status;
    private Integer name;
    private Integer excludedFromBundling;
    private Integer diameter;
    private Integer holeType;
    private Integer isCountersunk;
    private Integer isFlanged;
    private Integer countersinkDirection;
    private Integer flangeDirection;
    private Integer length;
    private Integer blind;
    private Integer sleevePinHole;
    private Integer coneAngle;
    private Integer tolerance;
    private Integer roughness;
    private Integer roughnessRz;
    private Integer diamTolerance;
    private Integer positionTolerance;
    private Integer circularity;
    private Integer concentricity;
    private Integer cylindricity;
    private Integer parallelism;
    private Integer perpendicularity;
    private Integer runout;
    private Integer totalRunout;
    private Integer straightness;
    private Integer symmetry;
    private Integer threaded;
    private Integer threadLength;
    private Integer volume;
    private Integer removedVolume;
    private Integer surfaceArea;
    private Integer minWallThickness;
    private Integer accessibility;
    private Integer holeDirectionAccessible;
    private Integer holeDirObstructionDistance;
    private Integer barPosition;
    private Integer clockAngle;
    private Integer draftAngle;
    private Integer normal;
    private Integer approachFrom;
    private Integer approachFromAlt;
    private Integer centroid;
    private Integer partingLineDrawDistance;
    private Integer userDefinedSetupAxis;
}
