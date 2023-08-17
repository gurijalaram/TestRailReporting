package enums;

public enum AssemblyTypeEnum {
    SUB_ASSEMBLY("Sub Assembly"),
    SUB_SUB_ASM("Sub Sub ASM"),
    TOP_LEVEL("Top Level");

    private final String assemblyType;

    AssemblyTypeEnum(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    /**
     * Gets assembly type name
     *
     * @return String
     */
    public String getAssemblyType() {
        return assemblyType;
    }
}