package com.apriori.cid.api.enums;

public enum Direction {
    ASC("ASC"),
    WRONG("WRONG"),
    DESC("DESC");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public String getEndpointString() {
        return direction;
    }
}
