package com.apriori.utils.web.components;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

/**
 * Represents a cell in a table row.
 */
public final class TableCellComponent extends CommonComponent {
    private static final By BY_CELL_TEXT = By.className("cell-text");

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
     * Gets the DOM element that contains the text of the cell.
     *
     * @return The element that contains the cell text.
     */
    private WebElement getCellText() {
        return getPageUtils().waitForElementToAppear(BY_CELL_TEXT, PageUtils.DURATION_FAST, getRoot());
    }

    /**
     * Gets the value of this cell given a parser.
     *
     * @param parse The parser that parses the cell content text.
     *
     * @return The value of this cell given a parser.
     */
    private <T> T getValue(final Function<String, T> parse) {
        String raw = getCellText().getText();
        return parse.apply(raw);
    }

    /**
     * Gets the value of this call as raw text.
     *
     * @return The value of the cell.
     */
    public String getAsText() {
        return getValue((s) -> s);
    }
}
