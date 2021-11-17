package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a table component.
 */
public final class TableComponent extends CommonComponent implements ComponentWithSpinner {
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
        return !getPageUtils().doesElementExist(By.className("loader"), getRoot());
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
        return getHeadersStream(By.cssSelector(".table-head .table-cell")).collect(Collectors.toList());
    }

    /**
     * Gets a header component by the id.
     *
     * @param id The id of the header component to retrieve.
     *
     * @return The table component header that holds the id or null if no such header exists.
     */
    public TableHeaderComponent getHeader(String id) {
        By query = By.cssSelector(String.format(".table-head .table-cell[data-header-id=%s]", id));
        return getHeadersStream(query).findFirst().orElse(null);
    }

    /**
     * Gets the collection of table rows.
     *
     * This will wait for the table to finish loading.
     *
     * @return The collection of the rows on the table.
     */
    public List<TableRowComponent> getRows() {
        getPageUtils().waitForCondition(this::isStable, Duration.ofSeconds(5));
        return getRoot().findElements(By.cssSelector(".table-body .table-row")).stream()
            .map((row) -> new TableRowComponent(this, row))
            .collect(Collectors.toList());
    }
}
