package com.apriori.css.entity.enums;

public enum Direction {
    ASC("SC"),
    DESC("DESC");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public String getEndpointString() {
        return direction;
    }
}
