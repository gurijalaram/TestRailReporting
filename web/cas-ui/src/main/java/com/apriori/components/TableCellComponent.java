package com.apriori.components;

import com.apriori.PageUtils;
import com.apriori.http.utils.Obligation;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents a cell in a table row.
 */
public final class TableCellComponent extends CommonComponent {
    private static final By BY_ANCHOR = By.cssSelector("a");
    private static final By BY_BUTTON = By.cssSelector("button");

    /**
     * Initializes a new instance of this object.
     *
     * @param row The table row that owns this cell.
     * @param root   The root element to attach for this component.
     */
    public TableCellComponent(final TableRowComponent row, final WebElement root) {
        super(row.getDriver(), root);
    }

    /**
     * Attempts to click the first child component found within the root.
     *
     * @return True if something was clicked.  False otherwise.
     */
    private boolean clickChild(By by) {
        WebElement anchor = Obligation.optional(() -> getPageUtils().waitForElementToAppear(by, PageUtils.DURATION_INSTANT, getRoot()));

        if (anchor == null) {
            return false;
        }

        anchor.click();
        return true;
    }

    /**
     * Attempts to click the contextual data in the cell.
     *
     * This method does one of three things.  If the cell has
     * an anchor tag underneath, then that anchor tag will be clicked.
     * If the cell has a button, then that button will be clicked.  Otherwise,
     * the cell itself is clicked.  In the case that there are multiple matching
     * items, then the first one is clicked.
     *
     * @return This component.
     */
    public TableCellComponent click() {
        if (clickChild(BY_ANCHOR)) {
            return this;
        }

        if (clickChild(BY_BUTTON)) {
            return this;
        }

        getPageUtils().waitForElementAndClick(getRoot());
        return this;
    }

    /**
     * Gets the underlying value as text.
     *
     * @return The cell value as text.
     */
    public String getValue() {
        return getRoot().getText();
    }

    /**
     * Gets whether the cell has a specific text value.
     *
     * This method is the equivalent of StringUtils.equals(getValue(), value).
     *
     * @param value The value to check.
     *
     * @return True if the cell's value matches value.  False otherwise.  Note that
     *         this is case-sensitive.
     */
    public boolean hasValue(final String value) {
        return StringUtils.equals(getValue(), value);
    }
}
