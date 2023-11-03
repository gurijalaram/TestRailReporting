package com.apriori.cid.ui.utils;

public enum DirectionEnum {

    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right");

    private final String direction;

    DirectionEnum(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }
}
