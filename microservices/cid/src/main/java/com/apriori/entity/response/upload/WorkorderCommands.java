package com.apriori.entity.response.upload;

public enum WorkorderCommands {

    LOAD_CAD_FILE("LOADCADFILE"),
    LOAD_CAD_METADATA("LOAD_CAD_METADATA"),
    GENERATE_PART_IMAGES("GENERATE_PART_IMAGES");

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
