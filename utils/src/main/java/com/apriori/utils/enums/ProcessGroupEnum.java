package com.apriori.utils.enums;

import java.util.Arrays;
import java.util.stream.Stream;

public enum ProcessGroupEnum {

    ADDITIVE_MANUFACTURING("Additive Manufacturing"),
    ASSEMBLY("Assembly"),
    BAR_TUBE_FAB("Bar & Tube Fab"),
    CASTING("Casting"),
    CASTING_DIE("Casting - Die"),
    CASTING_SAND("Casting - Sand"),
    CASTING_INVESTMENT("Casting - Investment"),
    COMPOSITES("Composites"),
    FORGING("Forging"),
    MULTI_SPINDLE_MACHINING("Multi-Spindle Machining"),
    PLASTIC_MOLDING("Plastic Molding"),
    POWDER_METAL("Powder Metal"),
    RAPID_PROTOTYPING("Rapid Prototyping"),
    RESOURCES("Resources"),
    ROLL_UP("Roll Up"),
    ROTO_BLOW_MOLDING("Roto & Blow Molding"),
    SHEET_METAL("Sheet Metal"),
    SHEET_METAL_HYDROFORMING("Sheet Metal - Hydroforming"),
    SHEET_METAL_STRETCH_FORMING("Sheet Metal - Stretch Forming"),
    SHEET_METAL_TRANSFER_DIE("Sheet Metal - Transfer Die"),
    SHEET_PLASTIC("Sheet Plastic"),
    STOCK_MACHINING("Stock Machining"),
    TWO_MODEL_MACHINING("2-Model Machining"),
    USER_GUIDED("User Guided"),
    WITHOUT_PG("Without PG");

    private final String processGroup;

    ProcessGroupEnum(String processGroup) {
        this.processGroup = processGroup;
    }

    public String getProcessGroup() {
        return this.processGroup;
    }

    public static String[] getNames() {
        return Stream.of(ProcessGroupEnum.values()).map(ProcessGroupEnum::getProcessGroup).toArray(String[]::new);
    }

    public static ProcessGroupEnum fromString(String pg) throws IllegalArgumentException {
        return Arrays.stream(ProcessGroupEnum.values())
            .filter(value -> value.processGroup.equals(pg))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' doesn't exist in enum ",  pg)));
    }
}
