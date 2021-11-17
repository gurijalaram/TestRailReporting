package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Represents the source list component.
 */
public final class SourceListComponent extends CommonComponent implements ComponentWithSpinner {
    /**
     * @inheritDoc
     */
    public SourceListComponent(final WebDriver driver, final WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the title text of the source list component.
     *
     * @return The title text.  Returns the empty string if no such title exists.
     */
    public String getTitle() {
        By query = By.className("apriori-source-list-title");
        Duration timeToWait = Duration.ofMillis(100);
        WebElement title = getPageUtils().waitForElementToAppearOptional(query, timeToWait, getRoot());
        return title == null ? "" : title.getText();
    }

    /**
     * Gets a value that determines if the current layout is loading ANYTHING.
     *
     * @return True if the layout is loading.  False otherwise.
     */
    @Override
    public boolean isStable() {
        return !getPageUtils().doesElementExist(By.className("loader"), getRoot());
    }

    /**
     * Gets the search input if it exists.
     *
     * @return The search input element, or null if this source list does not support search.
     */
    private WebElement getSearchInput() {
        return getPageUtils().waitForElementToAppearOptional(
            By.cssSelector(".apriori-source-list-search input"),
            Duration.ofMillis(100),
            getRoot()
        );
    }

    /**
     * Gets a value that determines if this source list can run the search operation.
     *
     * @return True if this list can search, false otherwise.
     */
    public boolean canSearch() {
        return getSearchInput() != null;
    }

    /**
     * Gets the placeholder for the search input.
     *
     * @return The search placeholder.  Returns the empty string if search is not supported.
     */
    public String getSearchPlaceholder() {
        WebElement search = getSearchInput();
        return search == null ? "" : search.getAttribute("placeholder");
    }

    /**
     * Enters text into the search field and runs the search.
     *
     * @return This component.
     *
     * @throws NoSuchElementException If the search is not available.
     */
    public SourceListComponent search(final String text) {
        WebElement search = getSearchInput();

        if (search == null) {
            throw new NoSuchElementException("The source list component you are accessing does not support search.");
        }

        getPageUtils().setValueOfElement(search, text, Keys.ENTER);
        getPageUtils().waitForCondition(this::isStable, Duration.ofSeconds(5));

        return this;
    }

    /**
     * Gets a value that indicates whether this list can be refreshed.
     *
     * @return True if the refresh button is visible, false otherwise.
     */
    public boolean canRefresh() {
        return getPageUtils().doesElementExist(By.className("apriori-source-list-refresh-button"), getRoot());
    }

    /**
     * Gets the table component.
     *
     * @return The table component.  Returns null if this list is using a card list layout.
     */
    public TableComponent getTable() {
        By query = By.className("apriori-table");
        Duration timeToWait = Duration.ofSeconds(1);
        WebElement tableRoot = getPageUtils().waitForElementToAppearOptional(query, timeToWait, getRoot());
        return tableRoot == null ? null : new TableComponent(getDriver(), tableRoot);
    }

    /**
     * Gets the paginator component.
     *
     * @return The paginator component.  Returns null if the paginator is turned off.
     */
    public PaginatorComponent getPaginator() {
        By query = By.className("paginator");
        Duration timeToWait = Duration.ofMillis(100);
        WebElement paginatorRoot = getPageUtils().waitForElementToAppearOptional(query, timeToWait, getRoot());
        return paginatorRoot == null ? null : new PaginatorComponent(getDriver(), paginatorRoot);
    }
}
