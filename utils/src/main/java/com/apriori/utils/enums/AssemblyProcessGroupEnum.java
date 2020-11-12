package com.apriori.utils.enums;

import java.util.stream.Stream;

@Deprecated
public enum AssemblyProcessGroupEnum {

    ASSEMBLY("Assembly"),
    ASSEMBLY_MOLDING("Assembly Molding"),
    MANUALLY_COSTED("Manually Costed");

    private final String processGroup;

    AssemblyProcessGroupEnum(String processGroup) {
        this.processGroup = processGroup;
    }

    public String getProcessGroup() {
        return this.processGroup;
    }

    public static String[] getNames() {
        return Stream.of(AssemblyProcessGroupEnum.values()).map(AssemblyProcessGroupEnum::getProcessGroup).toArray(String[]::new);
    }
}
