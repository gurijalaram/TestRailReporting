package main.java.enums;

public enum UnitsEnum {

    ENGLISH("ENGLISH"),
    SYSTEM("SYSTEM");

    private final String unit;

    UnitsEnum(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }
}
