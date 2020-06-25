package com.apriori.utils.enums;

public enum ComponentTypeEnum {

    PART("Part"),
    ASSEMBLY("Assembly"),
    ROLLUP("Roll-up"),
    DYNAMIC_ROLLUP("Dynamic Roll-up");

    private String componentType;

    ComponentTypeEnum(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentType() {
        return this.componentType;
    }
}
