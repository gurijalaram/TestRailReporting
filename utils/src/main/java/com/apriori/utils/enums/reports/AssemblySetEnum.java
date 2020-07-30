package com.apriori.utils.enums.reports;

public enum AssemblySetEnum {
    SUB_ASSEMBLY("SUB-ASSEMBLY (Initial)"),
    SUB_SUB_ASM("SUB-SUB-ASM (Initial)"),
    TOP_LEVEL("TOP-LEVEL (Initial)");

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
