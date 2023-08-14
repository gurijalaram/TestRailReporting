package com.apriori.enums;

public enum UnitsEnum {

    CGM("CGM"),
    CGS("CGS"),
    FPM("FPM"),
    FPS("FPS"),
    IOM("IOM"),
    IOS("IOS"),
    IPM("IPM"),
    IPS("IPS"),
    MGM("MGM"),
    MGS("MGS"),
    MKM("MKM"),
    MMGM("MMGM"),
    MMGS("MMGS"),
    MMKS("MMKS"),
    CUSTOM("Custom");

    private final String units;

    UnitsEnum(String units) {
        this.units = units;
    }

    public String getUnits() {
        return this.units;
    }
}
