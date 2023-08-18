package com.apriori.enums;

public enum WorkflowListColumns {
    Name(0),
    Description(1),
    Last_Modified_By(4),
    Schedule(3),
    Connector(5),
    Locked(9);

    private final int columnID;

    private WorkflowListColumns(int value) {
        this.columnID = value;
    }

    /**
     * Gets the integer value of a WorkflowListColumns enum entry.<p>
     * Use this method to obtain the ColumnID in the Schedule tab workflow list table.
     * @return Integer value of enum listing.
     */
    public int getColumnID() {
        return this.columnID;
    }

    /**
     * Obtain a WorkflowListColumns Enum value from its corresponding integer value.
     * @param columnID - WorkflowListColumns enum's integer value (See {@link WorkflowListColumns WorkflowListColumns} for enum/int mapping)
     * @return {@link WorkflowListColumns WorkflowListColumns}
     */
    public int getColumnByID(int columnID) {
        return this.columnID;
    }
}
