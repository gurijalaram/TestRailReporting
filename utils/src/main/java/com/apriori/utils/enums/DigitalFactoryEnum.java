package com.apriori.utils.enums;

import java.util.stream.Stream;

public enum DigitalFactoryEnum {

    APRIORI_BRAZIL("aPriori Brazil"),
    APRIORI_CHINA("aPriori China"),
    APRIORI_EASTERN_EUROPE("aPriori Eastern Europe"),
    APRIORI_GERMANY("aPriori Germany"),
    APRIORI_INDIA("aPriori India"),
    APRIORI_MEXICO("aPriori Mexico"),
    APRIORI_UNITED_KINGDOM("aPriori United Kingdom"),
    APRIORI_USA("aPriori USA"),
    APRIORI_WESTERN_EUROPE("aPriori Western Europe");

    private final String vpe;

    DigitalFactoryEnum(String vpe) {
        this.vpe = vpe;
    }

    public String getVpe() {
        return this.vpe;
    }

    public static String[] getNames() {
        return Stream.of(DigitalFactoryEnum.values()).map(DigitalFactoryEnum::getVpe).toArray(String[]::new);
    }
}
