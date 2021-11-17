package com.apriori.utils.web.components;

import org.openqa.selenium.WebElement;

/**
 * Represents a cell in a table row.
 */
public class TableCellComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param row The table row that owns this cell.
     * @param root   The root element to attach for this component.
     */
    protected TableCellComponent(TableRowComponent row, WebElement root) {
        super(row.getDriver(), root);
    }
}
