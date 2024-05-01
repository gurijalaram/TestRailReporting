package com.apriori.cir.ui.enums;

public enum AssemblySetEnum {
    SUB_ASSEMBLY("SUB-ASSEMBLY (Initial)"),
    SUB_ASSEMBLY_SHORT("SUB-ASSEMBLY"),
    SUB_ASSEMBLY_LONG("SUB-ASSEMBLY (Initial) [assembly] "),
    SUB_ASSEMBLY_LOWERCASE("Sub Assembly"),
    SUB_SUB_ASM("SUB-SUB-ASM (Initial)"),
    SUB_SUB_ASM_LONG("SUB-SUB-ASM (Initial) [assembly] "),
    SUB_SUB_ASM_SHORT("SUB-SUB-ASM"),
    SUB_SUB_ASM_LOWERCASE("Sub Sub ASM"),
    TOP_LEVEL("TOP-LEVEL (Initial)"),
    TOP_LEVEL_LONG("TOP-LEVEL (Initial) [assembly] "),
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