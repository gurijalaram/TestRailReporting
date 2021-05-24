package com.apriori.entity.response.upload;

public enum WorkorderCommands {

    COSTING("COSTING"),
    GENERATE_ASSEMBLY_IMAGES("GENERATE_ASSEMBLY_IMAGES"),
    GENERATE_PART_IMAGES("GENERATE_PART_IMAGES"),
    LOAD_CAD_FILE("LOADCADFILE"),
    LOAD_CAD_METADATA("LOAD_CAD_METADATA"),
    PUBLISH("PUBLISH");

    private final String workorderCommand;

    WorkorderCommands(String name) {
        this.workorderCommand = name;
    }

    /**
     * Gets report name from Enum
     *
     * @return String
     */
    public String getWorkorderCommand() {
        return this.workorderCommand;
    }
}
