package com.apriori.cas.ui.components;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents a header component in the table.
 */
public class TableHeaderComponent extends CommonComponent {
    private static final String ATTRIBUTE_HEADER_ID = "data-header-id";
    private static final By BY_CELL_CONTENT = By.className("cell-content");
    private static final String CLASS_SORT_DISABLED = "sort-disabled";

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
     * Gets the content element of the cell.
     *
     * @return The content element of the cell.
     */
    private WebElement getCellContent() {
        return getPageUtils().waitForElementToAppear(BY_CELL_CONTENT, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets a value that determines if the header can be sorted.
     *
     * @return True if this header can be sorted, false otherwise.
     */
    public boolean canSort() {
        return !getPageUtils().doesElementHaveClass(getCellContent(), CLASS_SORT_DISABLED);
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
        return getRoot().getAttribute(ATTRIBUTE_HEADER_ID);
    }
}
