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

    private final String digitalFactory;

    DigitalFactoryEnum(String digitalFactory) {
        this.digitalFactory = digitalFactory;
    }

    public String getDigitalFactory() {
        return this.digitalFactory;
    }

    public static String[] getNames() {
        return Stream.of(DigitalFactoryEnum.values()).map(DigitalFactoryEnum::getDigitalFactory).toArray(String[]::new);
    }
}
