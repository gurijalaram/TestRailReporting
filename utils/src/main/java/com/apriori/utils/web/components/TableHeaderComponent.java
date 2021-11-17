package com.apriori.utils.web.components;

import org.openqa.selenium.WebElement;

/**
 * Represents a header component in the table.
 */
public class TableHeaderComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param table The table that the header is a part of.
     * @param root  The root element to attach for this component.
     */
    public TableHeaderComponent(TableComponent table, WebElement root) {
        super(table.getDriver(), root);
    }

    /**
     * Gets the display name of the table header.
     *
     * @return The display name of the table header.
     */
    public String getName() {
        return getRoot().getText();
    }

    /**
     * Gets the id of the table header component.
     *
     * @return The id of this table header.
     */
    public String getId() {
        return getRoot().getAttribute("data-header-id");
    }
}
