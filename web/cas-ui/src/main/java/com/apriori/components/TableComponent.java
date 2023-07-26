package com.apriori.components;

import com.apriori.PageUtils;
import com.apriori.http.utils.Obligation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a table component.
 */
public final class TableComponent extends CommonComponent implements ComponentWithSpinner {
    private static final String ATTRIBUTE_DATA_HEADER_ID = "data-header-id";
    private static final String CSS_HEADERS = ".table-head .table-cell";

    private static final By BY_HEADERS = By.cssSelector(CSS_HEADERS);
    private static final By BY_ROWS = By.cssSelector(".table-body .table-row");
    private static final By BY_CHECKBOX_HEADER = By.cssSelector(".table-head .table-cell.checkbox-cell .checkbox");

    /**
     * @inheritDoc
     */
    public TableComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isStable() {
        return getPageUtils().findLoader(getRoot()) == null;
    }

    /**
     * Gets the stream list of headers.
     *
     * @param by The query for the headers.
     *
     * @return The stream that fires to a list of TableHeaderComponent objects.
     */
    private Stream<TableHeaderComponent> getHeadersStream(By by) {
        return getRoot().findElements(by).stream()
            .map((cell) -> new TableHeaderComponent(this, cell));
    }

    /**
     * Gets the collection of table headers.
     *
     * @return The collection of table headers.
     */
    public List<TableHeaderComponent> getHeaders() {
        return getHeadersStream(BY_HEADERS).collect(Collectors.toList());
    }

    /**
     * Gets a header component by the id.
     *
     * @param id The id of the header component to retrieve.
     *
     * @return The table component header that holds the id or null if no such header exists.
     */
    public TableHeaderComponent getHeader(String id) {
        By query = By.cssSelector(String.format("%s[%s=\"%s\"]", CSS_HEADERS, ATTRIBUTE_DATA_HEADER_ID, id));
        return getHeadersStream(query).findFirst().orElse(null);
    }

    /**
     * Gets the checkbox header for the table.
     *
     * @return The table header check component or null if table check selection is
     * not available.
     */
    public CheckboxComponent getCheckHeader() {
        return Obligation.optional(() ->
            new CheckboxComponent(
                this.getDriver(),
                getPageUtils().waitForElementToAppear(BY_CHECKBOX_HEADER, PageUtils.DURATION_INSTANT,
                getRoot())
            )
        );
    }

    /**
     * Gets the row stream.
     *
     * @return The row stream.
     */
    public Stream<TableRowComponent> getRows() {
        getPageUtils().waitForCondition(this::isStable, PageUtils.DURATION_LOADING);
        return getRoot().findElements(BY_ROWS).stream()
            .map((row) -> new TableRowComponent(this, row));
    }
}
