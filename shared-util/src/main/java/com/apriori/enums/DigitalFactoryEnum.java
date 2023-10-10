package com.apriori.enums;

import java.util.stream.Stream;

public enum DigitalFactoryEnum {

    APRIORI_BRAZIL("aPriori Brazil"),
    APRIORI_CANADA("aPriori Canada"),
    APRIORI_CHINA("aPriori China"),
    APRIORI_EASTERN_EUROPE("aPriori Eastern Europe"),
    APRIORI_FINLAND("aPriori Finland"),
    APRIORI_GERMANY("aPriori Germany"),
    APRIORI_INDIA("aPriori India"),
    APRIORI_IRELAND("aPriori Ireland"),
    APRIORI_MEXICO("aPriori Mexico"),
    APRIORI_UNITED_KINGDOM("aPriori United Kingdom"),
    APRIORI_USA("aPriori USA"),
    APRIORI_WESTERN_EUROPE("aPriori Western Europe");

    private final String digitalFactory;

    DigitalFactoryEnum(String digitalFactory) {
        this.digitalFactory = digitalFactory;
    }

    public static String[] getNames() {
        return Stream.of(DigitalFactoryEnum.values()).map(DigitalFactoryEnum::getDigitalFactory).toArray(String[]::new);
    }

    public String getDigitalFactory() {
        return this.digitalFactory;
    }
}
