package com.apriori.acs.api.models.response.workorders.genericclasses;

public enum WorkorderCommands {

    BOM_LOADER("BOM_LOADER"),
    COSTING("COSTING"),
    EDIT("EDIT"),
    GENERATE_ALL_IMAGES("GENERATEALLIMAGESDATACOMMAND"),
    GENERATE_ASSEMBLY_IMAGES("GENERATE_ASSEMBLY_IMAGES"),
    GENERATE_PART_IMAGES("GENERATE_PART_IMAGES"),
    GENERATE_SIMPLE_IMAGE_DATA("GENERATESIMPLEIMAGEDATA"),
    LOAD_CAD_FILE("LOADCADFILE"),
    LOAD_CAD_METADATA("LOAD_CAD_METADATA"),
    PUBLISH("PUBLISH"),
    DELETE("DELETE");

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
