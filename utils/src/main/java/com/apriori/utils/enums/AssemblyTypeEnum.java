package com.apriori.utils.enums;

public enum AssemblyTypeEnum {
    TOP_LEVEL("Top Level"),
    SUB_SUB_ASM("Sub Sub ASM"),
    SUB_ASSEMBLY("Sub Assembly");

    private final String assemblyType;

    AssemblyTypeEnum(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    public String getAssemblyType() {
        return assemblyType;
    }
}
