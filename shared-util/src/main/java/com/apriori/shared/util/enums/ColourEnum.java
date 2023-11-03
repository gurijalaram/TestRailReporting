package com.apriori.shared.util.enums;

public enum ColourEnum {

    RED("#ff0000"),
    YELLOW("#ffff00"),
    LIME("#00ff00"),
    AQUA("#00ffff"),
    BLUE("#0000ff"),
    FUCHSIA("#ff00ff"),
    WHITE("#ffffff"),
    HOLLYWOOD_CERISE("#ff0099"),
    ORANGE_PEEL("#ff9900"),
    SPRING_BUD("#99ff00"),
    MEDIUM_SPRING_GREEN("#00ff99"),
    DODGER_BLUE("#0099ff"),
    ELECTRIC_PURPLE("#9900ff"),
    NOBEL("#999999"),
    EGGPLANT("#990066"),
    GOLDEN_BROWN("#996600"),
    CHRISTI("#669900"),
    SHAMROCK_GREEN("#009966"),
    CERULEAN("#006699"),
    INDIGO("#660099"),
    BLACK("#000000"),
    CHINA_IVORY_A("#fff3cd"),
    CHINA_IVORY_B("#fff4d3");

    private final String colour;

    ColourEnum(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }
}
