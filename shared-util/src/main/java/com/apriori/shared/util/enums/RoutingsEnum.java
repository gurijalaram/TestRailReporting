package com.apriori.shared.util.enums;

import java.util.stream.Stream;

public enum RoutingsEnum {

    MATERIALSTOCK_TURRETPRESS_BENDBRAKE("Material Stock / Turret Press / Bend Brake"),
    MATERIALSTOCK_LASERPUNCH_BENDBRAKE("Material Stock / Laser Punch / Bend Brake"),
    CTL_LASERPUNCH_BEND("[CTL]/Laser Punch/[Bend]");

    private final String routing;

    RoutingsEnum(String routing) {
        this.routing = routing;
    }

    public static String[] getNames() {
        return Stream.of(RoutingsEnum.values()).map(RoutingsEnum::getRouting).toArray(String[]::new);
    }

    public String getRouting() {
        return this.routing;
    }
}
