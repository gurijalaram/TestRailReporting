package com.apriori.utils.enums.reports;

public enum AssemblySetEnum {
    SUB_ASSEMBLY("SUB-ASSEMBLY (Initial)"),
    SUB_ASSEMBLY_SHORT("SUB-ASSEMBLY"),
    SUB_SUB_ASM("SUB-SUB-ASM (Initial)"),
    SUB_SUB_ASM_SHORT("SUB-SUB-ASM"),
    TOP_LEVEL("TOP-LEVEL (Initial)"),
    TOP_LEVEL_SHORT("TOP-LEVEL"),
    PISTON_ASSEMBLY("PISTON_ASSEMBLY");

    private final String assemblySetName;

    AssemblySetEnum(String assemblySetName) {
        this.assemblySetName = assemblySetName;
    }

    /**
     * Gets assembly set name
     *
     * @return String
     */
    public String getAssemblySetName() {
        return this.assemblySetName;
    }
}
