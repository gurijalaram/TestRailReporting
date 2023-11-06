package com.apriori.cid.ui.utils;

public enum ComparisonDeltaEnum {

    ARROW_UP("arrow-up"),
    ARROW_DOWN("arrow-down"),
    NOT_EQUALS("not-equals"),
    GREEN("var(--green)"),
    RED("var(--red)"),
    CYAN("var(--cyan)"),
    MINUS("minus");

    String delta;

    ComparisonDeltaEnum(String delta) {
        this.delta = delta;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }
}
