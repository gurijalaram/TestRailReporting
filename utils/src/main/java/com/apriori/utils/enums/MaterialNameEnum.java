package com.apriori.utils.enums;

public enum MaterialNameEnum {

    ABS("ABS"),
    ABS_10_GLASS("ABS, 10% Glass"),
    ABS_PLATING("ABS, Plating"),
    ALUMINIUM_ANSI_AL380("Aluminum, ANSI AL380.0"),
    ALUMINIUM_ANSI_7075("Aluminum, ANSI 7075"),
    ALUMINIUM_ANSI_1050A("Aluminum, ANSI 1050A"),
    ALUMINIUM_ANSI_6061("Aluminum, ANSI 6061"),
    ALUMINIUM_ALSI10MG("Aluminum AlSi10Mg"),

    BRASS_YELLOW_270("Brass, Yellow 270"),

    COPPER_UNS_C11000("Copper, UNS C11000"),
    COPPER_UNS_C28000("Copper, UNS C28000"),

    HIPS_EXTRUSION("HIPS Extrusion"),

    INCONEL_625("Inconel 625"),

    NYLON_TYPE_6("Nylon, Type 6"),

    PET_30_GLASS("PET 30% Glass"),

    POLYETHYLENE_HDPE("Polyethylene, High Density, HDPE"),

    POLYETHERETHERKETONE_PEEK("Polyetheretherketone, PEEK"),
    POLYURETHANE_POLYMERIC_MDI("Polyurethane, Polymeric MDI"),

    STAINLESS_STEEL_AISI_316("Stainless Steel, AISI 316"),
    STAINLESS_STEEL_440B("Stainless Steel, 440B"),

    STEEL_HOT_WORKED_AISI1010("Steel, Hot Worked, AISI 1010"),
    STEEL_HOT_WORKED_AISI1095("Steel, Hot Worked, AISI 1095"),

    STEEL_COLD_WORKED_AISI1010("Steel, Cold Worked, AISI 1010"),
    STEEL_COLD_WORKED_AISI1020("Steel, Cold Worked, AISI 1020"),
    STEEL_F0005("F-0005"),
    STEEL_F0005_SPONGE("F-0005 Sponge"),
    STEEL_FN025("FN-0205"),

    UNSATURATED_POLYESTER_CF50("Unsaturated Polyester, CF50"),

    VISIJET_M3_BLACK("Visijet M3 Black");

    private final String materialName;

    MaterialNameEnum(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return this.materialName;
    }
}
