package com.apriori.utils.enums.reports;

public enum ListNameEnum {
    CREATED_BY("Created By"),
    LAST_MODIFIED_BY("Last Modified By"),
    PARTS("Select Parts "),
    PARTS_NO_SPACE("Select Parts");

    private String listName;

    ListNameEnum(String listName) {
        this.listName = listName;
    }

    /**
     * Gets List name
     *
     * @return String
     */
    public String getListName() {
        return this.listName;
    }
}
