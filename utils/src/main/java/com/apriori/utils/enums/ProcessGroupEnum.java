package com.apriori.utils.enums;

import java.util.stream.Stream;

public enum ProcessGroupEnum {

    ADDITIVE_MANUFACTURING("Additive Manufacturing"),
    BAR_TUBE_FAB("Bar & Tube Fab"),
    CASTING("Casting"),
    CASTING_DIE("Casting - Die"),
    CASTING_SAND("Casting - Sand"),
    COMPOSITES("Composites"),
    FORGING("Forging"),
    PLASTIC_MOLDING("Plastic Molding"),
    POWDER_METAL("Powder Metal"),
    RAPID_PROTOTYPING("Rapid Prototyping"),
    ROTO_BLOW_MOLDING("Roto & Blow Molding"),
    SHEET_METAL("Sheet Metal"),
    SHEET_METAL_HYDROFORMING("Sheet Metal - Hydroforming"),
    SHEET_METAL_STRETCH_FORMING("Sheet Metal - Stretch Forming"),
    SHEET_METAL_TRANSFER_DIE("Sheet Metal - Transfer Die"),
    SHEET_PLASTIC("Sheet Plastic"),
    STOCK_MACHINING("Stock Machining"),
    TWO_MODEL_MACHINING("2-Model Machining"),
    WITHOUT_PG("Without PG"),
    ASSEMBLY("Assembly");

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
}
