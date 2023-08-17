package com.utils;

/**
 * @author cfrith
 */

public enum ToleranceEnum {

    CIRCULARITY("Circularity"),
    PARALLELISM("Parallelism"),
    CONCENTRICITY("Concentricity"),
    CYLINDRICITY("Cylindricity"),
    DIAMTOLERANCE("Diam Tolerance"),
    FLATNESS("Flatness"),
    PERPENDICULARITY("Perpendicularity"),
    TRUEPOSITION("True Position"),
    PROFILESURFACE("Profile of Surface"),
    ROUGHNESSRA("Roughness Ra"),
    ROUGHNESSRZ("Roughness Rz"),
    RUNOUT("Runout"),
    STRAIGHTNESS("Straightness"),
    SYMMETRY("Symmetry"),
    TOLERANCE("Tolerance"),
    TOTALRUNOUT("Total Runout"),
    BEND_ANGLE_TOLERANCE("Bend Angle Tolerance");

    private final String toleranceName;

    ToleranceEnum(String toleranceName) {
        this.toleranceName = toleranceName;
    }

    public String getToleranceName() {
        return this.toleranceName;
    }
}