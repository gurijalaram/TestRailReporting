package com.apriori.utils.web.components;

import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents a row in a table
 */
public final class TableRowComponent extends CommonComponent {
    private static final String ATTRIBUTE_HEADER_ID = "data-header-id";
    private static final String ATTRIBUTE_CHECK_ICON = "data-icon";
    private static final String ATTRIBUTE_CHECK_ICON_CHECKED = "check-square";
    private static final String CSS_CELL = ".table-cell";
    private static final By BY_CHECKBOX_CELL = By.cssSelector(".checkbox-cell svg");

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

    /**
     * Gets the underlying svg element underneath the checkbox.
     *
     * If the table does not support checkboxes, then this will
     * return null.
     *
     * @return The svg element under the checkbox cell or null if the row does not support the checkbox cell.
     */
    private WebElement getCheckboxSvgElement() {
        return Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_CHECKBOX_CELL, PageUtils.DURATION_FAST, getRoot()));
    }

    /**
     * Gets a flag that determines if this row is selected.
     *
     * @return True if this row is checked
     */
    public boolean isChecked() {
        WebElement check = getCheckboxSvgElement();
        return check != null && StringUtils.equals(check.getAttribute(ATTRIBUTE_CHECK_ICON), ATTRIBUTE_CHECK_ICON_CHECKED);
    }

    /**
     * Sets the check state of the row.
     *
     * If the row is already in the specified check state, then this
     * method does nothing.
     *
     * @param checked The check state to verify.
     *
     * @return This component.
     */
    public TableRowComponent check(boolean checked) {
        boolean current = isChecked();

        if (current == checked) {
            return this;
        }

        // The underlying checkbox component is a bit weird as it is not an actual
        // input component and is a svg instead.
        getPageUtils().scrollWithJavaScript(getRoot(), true);
        getPageUtils().waitForElementAndClick(getCheckboxSvgElement());
        getPageUtils().waitForCondition(this::isChecked, PageUtils.DURATION_FAST);
        return this;
    }
}
