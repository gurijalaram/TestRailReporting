package com.apriori.cas.ui.components;

import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the source list component.
 */
public final class SourceListComponent extends CommonComponent implements ComponentWithSpinner {

    private static final By BY_TITLE = By.className("apriori-source-list-title");
    private static final By BY_SEARCH_FIELD = By.cssSelector(".apriori-source-list-search");
    private static final By BY_TABLE = By.className("apriori-table");
    private static final By BY_PAGINATOR = By.className("paginator");
    private static final By BY_REFRESH = By.className("apriori-source-list-refresh-button");
    private static final By BY_LAYOUT_TABLE = By.className("apriori-source-list-layout-table-button");
    private static final By BY_CARD_GRID = By.className("apriori-source-list-card-grid");

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
        WebElement title = Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_TITLE, PageUtils.DURATION_FAST, getRoot()));
        return title == null ? "" : title.getText();
    }

    /**
     * Gets a value that determines if the current layout is loading ANYTHING.
     *
     * @return True if the layout is loading.  False otherwise.
     */
    @Override
    public boolean isStable() {
        return getPageUtils().findLoader(getRoot()) == null;
    }

    /**
     * Gets a value that indicates whether this list can be refreshed.
     *
     * @return True if the refresh button is visible, false otherwise.
     */
    public boolean canRefresh() {
        return Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_REFRESH, PageUtils.DURATION_INSTANT, getRoot())) != null;
    }

    /**
     * Selects a specific layout.
     *
     * @param layout The query for the layout to select.
     *
     * @return This component.
     */
    private SourceListComponent selectLayout(final By layout) {
        WebElement button = getPageUtils().waitForElementToAppear(layout, PageUtils.DURATION_INSTANT, getRoot());
        button.click();
        return this;
    }

    /**
     * Selects the table layout.
     *
     * If the table layout is already selected, then this method does nothing.
     *
     * @return This component.
     */
    public SourceListComponent selectTableLayout() {
        return selectLayout(BY_LAYOUT_TABLE);
    }

    /**
     * Gets the search input if it exists.
     *
     * @return The search input element, or null if this source list does not support search.
     */
    public SearchFieldComponent getSearch() {
        WebElement search = Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_SEARCH_FIELD, PageUtils.DURATION_INSTANT, getRoot()));
        if (search != null) {
            return new SearchFieldComponent(getDriver(), search);
        }
        return null;
    }

    /**
     * Gets the table component.
     *
     * @return The table component.  Returns null if this list is using a card list layout.
     */
    public TableComponent getTable() {
        WebElement tableRoot = Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_TABLE, PageUtils.DURATION_SLOW, getRoot()));
        if (tableRoot != null) {
            return new TableComponent(getDriver(), tableRoot);
        }
        return null;
    }

    /**
     * Gets the paginator component.
     *
     * @return The paginator component.  Returns null if the paginator is turned off.
     */
    public PaginatorComponent getPaginator() {
        WebElement paginatorRoot = Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_PAGINATOR, PageUtils.DURATION_FAST, getRoot()));
        if (paginatorRoot != null) {
            return new PaginatorComponent(getDriver(), paginatorRoot);
        }
        return null;
    }

    /**
     * Gets the card grid component
     *
     * @return The card grid component. Returns null if this list is using a table list layout.
     */
    public CardsViewComponent getCardGrid() {
        WebElement cardGridRoot = Obligation.optional(() -> getPageUtils().waitForElementToAppear(BY_CARD_GRID, PageUtils.DURATION_LOADING, getRoot()));
        if (cardGridRoot != null) {
            return new CardsViewComponent(getDriver(), cardGridRoot);
        }
        return null;
    }
}
