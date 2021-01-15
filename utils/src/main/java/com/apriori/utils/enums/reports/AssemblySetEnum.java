package com.apriori.utils.enums.reports;

public enum AssemblySetEnum {
    SUB_ASSEMBLY("SUB-ASSEMBLY (Initial)"),
    SUB_ASSEMBLY_SHORT("SUB-ASSEMBLY"),
    SUB_ASSEMBLY_LOWERCASE("Sub Assembly"),
    SUB_SUB_ASM("SUB-SUB-ASM (Initial)"),
    SUB_SUB_ASM_SHORT("SUB-SUB-ASM"),
    SUB_SUB_ASM_LOWERCASE("Sub Sub ASM"),
    TOP_LEVEL("TOP-LEVEL (Initial)"),
    TOP_LEVEL_SHORT("TOP-LEVEL"),
    TOP_LEVEL_LOWERCASE("Top Level"),
    TOP_LEVEL_MULTI_VPE("TOP-LEVEL (Multi VPE)"),
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
