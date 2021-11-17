package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Represents a row in a table
 */
public final class TableRowComponent extends CommonComponent {
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
        String queryByHeaderId = String.format(".table-cell[data-header-id=%s]", header);
        WebElement cell = getPageUtils().waitForElementToAppearOptional(
            By.cssSelector(queryByHeaderId),
            Duration.ofMillis(10),
            getRoot()
        );
        return cell == null ? null : new TableCellComponent(this, cell);
    }
}
