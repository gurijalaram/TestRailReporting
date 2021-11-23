package com.apriori.utils.web.components;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents a row in a table
 */
public final class TableRowComponent extends CommonComponent {
    private static final String ATTRIBUTE_HEADER_ID = "data-header-id";
    private static final String CSS_CELL = ".table-cell";

    /**
     * Initializes a new instance of this object.
     *
     * @param owner The table component that owns the row.
     * @param root   The root element to attach for this component.
     */
    public TableRowComponent(TableComponent owner, WebElement root) {
        super(owner.getDriver(), root);
    }

    /**
     * Gets the cell for a given header.
     *
     * @param header The id of the header to retrieve.
     *
     * @return The cell with the given id header.
     */
    public TableCellComponent getCell(String header) {
        String queryByHeaderId = String.format("%s[%s=%s]", CSS_CELL, ATTRIBUTE_HEADER_ID, header);
        WebElement cell = getPageUtils().waitForElementToAppear(
            By.cssSelector(queryByHeaderId),
            PageUtils.DURATION_INSTANT,
            getRoot()
        );
        return new TableCellComponent(this, cell);
    }
}
