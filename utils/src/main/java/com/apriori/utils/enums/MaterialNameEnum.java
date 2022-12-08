package com.apriori.utils.enums;

public enum MaterialNameEnum {

    ALUMINIUM_AL380("Aluminum, ANSI AL380.0"),
    ALUMINIUM_ANSI_7075("Aluminum, ANSI 7075"),
    ALUMINIUM_ANSI_1050A("Aluminum, ANSI 1050A"),
    ALUMINIUM_ANSI_6061("Aluminum, ANSI 6061"),

    BRASS_YELLOW_270("Brass, Yellow 270"),

    COPPER_UNS_C11000("Copper, UNS C11000"),
    COPPER_UNS_C28000("Copper, UNS C28000"),

    POLYETHYLENE_HDPE("Polyethylene, High Density, HDPE"),

    POLYETHERETHERKETONE_PEEK("Polyetheretherketone, PEEK"),

    STAINLESS_STEEL_AISI_316("Stainless Steel, AISI 316"),
    STAINLESS_STEEL_440B("Stainless Steel, 440B");

    private final String materialName;

    MaterialNameEnum(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return this.materialName;
    }
}
