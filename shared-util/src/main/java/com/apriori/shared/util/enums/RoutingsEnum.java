package com.apriori.shared.util.enums;

import java.util.EnumSet;
import java.util.stream.Stream;

public enum RoutingsEnum {

    THREE_AXIS_MILL_ROUTING("3 Axis Mill Routing"),
    FOUR_AXIS_MILL_ROUTING("4 Axis Mill Routing"),
    FIVE_AXIS_MILL_ROUTING("5 Axis Mill Routing"),
    TWO_AXIS_LATHE_THREE_AXIS_MILL("2AL+3AM Routing"),
    TWO_AXIS_LATHE_FOUR_AXIS_MILL("2AL+4AM Routing"),
    TWO_AXIS_LATHE_FIVE_AXIS_MILL("2AL+5AM Routing"),
    THREE_AXIS_LATHE_ROUTING("3 Axis Lathe Routing"),
    MILL_TURN_ROUTING("Mill Turn Routing"),
    DRILL_PRESS_ROUTING("Drill Press Routing"),

    SAND_CASTING("Sand Casting"),
    DIE_CASTING("Die Casting"),
    PERMANENT_MOLD("Permanent Mold"),

    HIGH_PRESSURE_DIE_CAST("High Pressure Die Cast"),
    GRAVITY_DIE_CAST("Gravity Die Cast"),

    BAND_SAW("Band Saw"),
    ABRASIVE_WHEEL_CUT("Abrasive Wheel Cut"),

    CORE_MACHINING_WATERJET_TRIM("Core Machining / Waterjet Trim"),

    CLOSED_DIE_FORGING("Closed Die Forging"),
    RING_ROLLED_FORGING("Ring Rolled Forging"),

    INJECTION_MOLD("Injection Mold"),
    REACTION_INJECTION_MOLD("Reaction Injection Mold"),
    STRUCTURAL_FOAM_MOLD("Structural Foam Mold"),
    COMPRESSION_MOLD("Compression Mold"),

    THREE_D_PRINTING("3D Printing"),
    SELECTIVE_LASER_SINTERING("Selective Laser Sintering"),
    STEREOLITHOGRAPHY("Stereolithography"),

    BLOW_MOLDING("Blow Molding"),
    ROTATIONAL_MOLDING("Rotational Molding"),

    CTL_CO2LASER_BEND("[CTL]/CO2 Laser/[Bend]"),
    CTL_FIBERLASER_BEND("[CTL]/Fiber Laser/[Bend]"),
    CTL_LASERPUNCH_BEND("[CTL]/Laser Punch/[Bend]"),
    CTL_PLASMA_DESLAG_BEND("[CTL]/Plasma/[Deslag]/[Bend]"),
    CTL_PLASMAPUNCH_DESLAG_BEND("[CTL]/Plasma Punch/[Deslag]/[Bend]"),
    CTL_OXYFUEL_DESLAG_BEND("[CTL]/Oxyfuel/[Deslag]/[Bend]"),
    CTL_WATERJET_BEND("[CTL]/Waterjet/[Bend]"),
    CTL_TURRET_BEND("[CTL]/Turret/[Bend]"),
    CTL_TWOAXISROUTER_BEND("[CTL]/2 Axis Router/[Bend]"),
    STAGE_TOOLING("Stage Tooling"),
    PROG_DIE("Prog Die"),
    CTL_SHEAR_PRESS("[CTL]/Shear/Press"),
    CTL_SHEAR_CHEMICALMILL("[CTL]/Shear/Chemical Mill"),
    TANDEM_DIE("Tandem Die"),
    CTL_BEND("[CTL]/[Bend]"),

    LASERCUT_FLUIDCELLROUTING("Laser Cut - Fluid Cell Routing"),
    ROUTERCUT_FLUIDCELLROUTING("Router Cut - Fluid Cell Routing"),
    OFFLINEBLANK_FLUIDCELLROUTING("Offline Blank - Fluid Cell Routing"),
    LASERCUT_DEEPDRAWROUTING("Laser Cut - Deep Draw Routing"),
    ROUTERCUT_DEEPDRAWROUTING("Router Cut - Deep Draw Routing"),
    OFFLINEBLANK_DEEPDRAW_ROUTING("Offline Blank - Deep Draw Routing"),

    STRETCH_FORM_TRAVERSE("Stretch Form Transverse"),
    STRETCH_FORM_LONGITUDINAL("Stretch Form Longitudinal"),

    SINGLE_STATION_THERMOFORMING("Single Station Thermoforming"),
    SHUTTLE_STATION_THERMOFORMING("Shuttle Station Thermoforming"),
    THREE_STATION_ROTARY_THERMOFORMING("3 Station Rotary Thermoforming"),
    FOUR_STATION_ROTARY_THERMOFORMING("4 Station Rotary Thermoforming"),

    THREE_AXIS_MILL_DRILL_PRESS_ROUTING("3AM+Drill Press Routing"),
    THREE_AXIS_MILL_FOUR_AXIS_MILL_ROUTING("3AM+4AM Routing"),
    THREE_AXIS_MILL_FIVE_AXIS_MILL_ROUTING("3AM+5AM Routing"),
    TWO_ABFL_AND_THREEAM_ROUTING("2ABFL and 3AM routing"),
    THREE_ABFL_ROUTING("3ABFL routing"),

    MATERIALSTOCK_TURRETPRESS_BENDBRAKE("Material Stock / Turret Press / Bend Brake"),
    MATERIALSTOCK_LASERPUNCH_BENDBRAKE("Material Stock / Laser Punch / Bend Brake"),

    STOPPER("fjfj");

    public static EnumSet<RoutingsEnum> twoModelMachining = EnumSet.of(
        THREE_AXIS_MILL_ROUTING, FOUR_AXIS_MILL_ROUTING, FIVE_AXIS_MILL_ROUTING, TWO_AXIS_LATHE_THREE_AXIS_MILL,
        TWO_AXIS_LATHE_FOUR_AXIS_MILL, TWO_AXIS_LATHE_FIVE_AXIS_MILL, THREE_AXIS_LATHE_ROUTING, MILL_TURN_ROUTING, DRILL_PRESS_ROUTING);

    public static EnumSet<RoutingsEnum> casting = EnumSet.of(SAND_CASTING, DIE_CASTING, PERMANENT_MOLD);

    public static EnumSet<RoutingsEnum> castingDie = EnumSet.of(HIGH_PRESSURE_DIE_CAST, GRAVITY_DIE_CAST);

    public static EnumSet<RoutingsEnum> castingInvestment = EnumSet.of(BAND_SAW, ABRASIVE_WHEEL_CUT);

    public static EnumSet<RoutingsEnum> composites = EnumSet.of(FIVE_AXIS_MILL_ROUTING, CORE_MACHINING_WATERJET_TRIM);

    public static EnumSet<RoutingsEnum> forging = EnumSet.of(CLOSED_DIE_FORGING, RING_ROLLED_FORGING);

    public static EnumSet<RoutingsEnum> plasticMolding = EnumSet.of(INJECTION_MOLD, REACTION_INJECTION_MOLD, STRUCTURAL_FOAM_MOLD, COMPRESSION_MOLD);

    public static EnumSet<RoutingsEnum> rapidPrototyping = EnumSet.of(THREE_D_PRINTING, SELECTIVE_LASER_SINTERING, STEREOLITHOGRAPHY);

    public static EnumSet<RoutingsEnum> rotoBlowMolding = EnumSet.of(BLOW_MOLDING, ROTATIONAL_MOLDING);

    public static EnumSet<RoutingsEnum> sheetMetal = EnumSet.of(
        CTL_CO2LASER_BEND, CTL_FIBERLASER_BEND, CTL_LASERPUNCH_BEND, CTL_PLASMA_DESLAG_BEND, CTL_PLASMAPUNCH_DESLAG_BEND, CTL_OXYFUEL_DESLAG_BEND, CTL_WATERJET_BEND,
        CTL_TURRET_BEND, CTL_TWOAXISROUTER_BEND, STAGE_TOOLING, PROG_DIE, CTL_SHEAR_PRESS,CTL_SHEAR_CHEMICALMILL, TANDEM_DIE, CTL_BEND);

    public static EnumSet<RoutingsEnum> sheetMetalHydroforming = EnumSet.of(
        LASERCUT_FLUIDCELLROUTING, ROUTERCUT_FLUIDCELLROUTING, OFFLINEBLANK_FLUIDCELLROUTING, LASERCUT_DEEPDRAWROUTING, ROUTERCUT_DEEPDRAWROUTING, OFFLINEBLANK_DEEPDRAW_ROUTING);

    public static EnumSet<RoutingsEnum> sheetMetalStretchForming = EnumSet.of(STRETCH_FORM_TRAVERSE, STRETCH_FORM_LONGITUDINAL);

    public static EnumSet<RoutingsEnum> sheetPlastic = EnumSet.of(
        SINGLE_STATION_THERMOFORMING, SHUTTLE_STATION_THERMOFORMING, THREE_STATION_ROTARY_THERMOFORMING, FOUR_STATION_ROTARY_THERMOFORMING);

    public static EnumSet<RoutingsEnum> stockMachining = EnumSet.of(
        THREE_AXIS_MILL_ROUTING, FOUR_AXIS_MILL_ROUTING, FIVE_AXIS_MILL_ROUTING, TWO_AXIS_LATHE_THREE_AXIS_MILL, TWO_AXIS_LATHE_FOUR_AXIS_MILL, TWO_AXIS_LATHE_FIVE_AXIS_MILL,
        THREE_AXIS_LATHE_ROUTING, MILL_TURN_ROUTING, THREE_AXIS_MILL_DRILL_PRESS_ROUTING, THREE_AXIS_MILL_FOUR_AXIS_MILL_ROUTING,
        THREE_AXIS_MILL_FIVE_AXIS_MILL_ROUTING, TWO_ABFL_AND_THREEAM_ROUTING, THREE_ABFL_ROUTING);


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
