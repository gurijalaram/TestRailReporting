package com.apriori.cid.ui.utils;

public enum OverridesEnum {
    CIRCULARITY("circularity"),
    CONCENTRICITY("concentricity"),
    CYLINDRICITY("cylindricity"),
    FLATNESS("cylindricity"),
    PARALLELISM("parallelism"),
    PERPENDICULARITY("perpendicularity"),
    POSITION_TOLERANCE("position"),
    PROFILE_OF_SURFACE("profile"),
    RUNOUT("runout"),
    TOTAL_RUNOUT("totalRunout"),
    STRAIGHTNESS("straightness"),
    SYMMETRY("symmetry"),
    COORDINATE("coordinate"),
    DIAMETER_TOLERANCE("diameter"),
    BEND_ANGLE("bendAngle"),
    ROUGHNESS_RA("roughnessRa"),
    ROUGHNESS_RZ("roughnessRz");

    private final String overrides;

    OverridesEnum(String overrides) {
        this.overrides = overrides;
    }

    public String getOverrides() {
        return this.overrides;
    }
}
