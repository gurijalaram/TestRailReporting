package com.apriori.utils.enums;

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

    private final String tolerance;

    ToleranceEnum(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getTolerance() {
        return this.tolerance;
    }
}
