package com.utils;

public enum PartNamesEnum {

    CENTRE_BOLT("centre bolt"),
    CENTRE_WASHER("centre washer"),
    DISPLAY("display"),
    GASKET("gasket"),
    HANDLE("Handle"),
    LEFT_PADDLE("left paddle"),
    LEFT_COVER("left cover"),
    STEER_WHEEL_SUPPORT("steer wheel support"),
    SEAT_LOCK("seat lock"),
    MECHANISM_BODY("mechanism body"),
    PADDLE_BAR("paddle bar"),
    LEG_COVER("leg cover"),
    LEG("leg"),
    RIGHT_PADDLE("right paddle"),
    WASHER("washer"),
    SEAT("seat"),
    PIN("pin");

    private final String partName;

    PartNamesEnum(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return this.partName;
    }
}
