package com.apriori.pageobjects.reports.pages.view.enums;

public enum AssemblySetEnum {
    SUB_ASSEMBLY("SUB-ASSEMBLY (Initial)"),
    SUB_SUB_ASM("SUB-SUB-ASM (Initial)"),
    TOP_LEVEL("TOP-LEVEL (Initial)"),
    PISTON_ASSEMBLY("PISTON_ASSEMBLY (Initial) [assembly]");

    private String assemblySetName;

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
