package com.apriori.cid.ui.utils;

public enum ColourEnum {
    POMEGRANATE("#f44336"),
    AMARANTH("#e91e63"),
    SEANCE("#9c27b0"),
    PURPLE_HEART("#673Ab7"),
    SAN_MARINO("#3f51b5"),
    DODGER_BLUE("#2196f3"),
    CERULEAN("#03a9f4"),
    ROBINS_BLUE("#00bcd4"),
    PERSIAN_GREEN("#009688"),
    FRUIT_SALAD("#4caf50"),
    SUSHI("#8Bc34a"),
    PEAR("#cddc39"),
    YELLOW("#ffeb3b"),
    YELLOW_LIGHT("#fff4d3"),
    AMBER("#ffc107"),
    ORANGE_PEEL("#ff9800"),
    ORANGE("#ff5722"),
    ROMAN_COFFEE("#795548"),
    LYNCH("##607d8b"),
    PLACEBO_BLUE("#ecf7fc");

    private final String colour;

    ColourEnum(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }
}
