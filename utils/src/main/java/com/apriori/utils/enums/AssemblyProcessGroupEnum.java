package com.apriori.utils.enums;

import java.util.stream.Stream;

public enum AssemblyProcessGroupEnum {

    ASSEMBLY("Assembly"),
    ASSEMBLY_MOLDING("Assembly Molding"),
    MANUALLY_COSTED("Manually Costed");

    private final String assemblyProcessGroup;

    AssemblyProcessGroupEnum(String assemblyProcessGroup) {
        this.assemblyProcessGroup = assemblyProcessGroup;
    }

    public String getProcessGroup() {
        return this.assemblyProcessGroup;
    }

    public static String[] getNames() {
        return Stream.of(AssemblyProcessGroupEnum.values()).map(AssemblyProcessGroupEnum::getProcessGroup).toArray(String[]::new);
    }
}
