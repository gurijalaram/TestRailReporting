package com.apriori.components;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents a paginator that can be used to change pages.
 */
public class PaginatorComponent extends CommonComponent {
    private static final By BY_PAGE_SIZE = By.className("apriori-select");
    private static final By BY_PAGE_CONTROLS = By.className("page-controls");
    private static final int INDEX_FIRST_PAGE = 0;
    private static final int INDEX_PREVIOUS_PAGE = 1;
    private static final int INDEX_NEXT_PAGE = 2;
    private static final int INDEX_LAST_PAGE = 3;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     */
    public PaginatorComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the page size drop-down.
     *
     * @return The page size drop-down.
     */
    public SelectComponent getPageSize() {
        WebElement pageSizeDrop = getPageUtils().waitForElementToAppear(BY_PAGE_SIZE, PageUtils.DURATION_INSTANT, getRoot());
        return new SelectComponent(getDriver(), pageSizeDrop);
    }

    /**
     * Gets the underlying page controls.
     *
     * @return The underlying page controls.
     */
    private List<WebElement> getPageControls() {
        WebElement pageControls = getPageUtils().waitForElementToAppear(BY_PAGE_CONTROLS, PageUtils.DURATION_INSTANT, getRoot());
        return pageControls.findElements(By.xpath("*"));
    }

    /**
     * Clicks the page control at a given index.
     *
     * @param index The index of the page control to click.
     *
     * @return This object.
     */
    private PaginatorComponent clickPageControl(int index) {
        List<WebElement> controls = getPageControls();
        getPageUtils().waitForElementAndClick(controls.get(index));
        return this;
    }

    /**
     * Clicks the first page double back chevron.
     *
     * @return This object.
     */
    public PaginatorComponent clickFirstPage() {
        return clickPageControl(INDEX_FIRST_PAGE);
    }

    /**
     * Clicks the previous page back chevron.
     *
     * @return This object.
     */
    public PaginatorComponent clickPreviousPage() {
        return clickPageControl(INDEX_PREVIOUS_PAGE);
    }

    /**
     * Clicks the next page forward chevron.
     *
     * @return This object.
     */
    public PaginatorComponent clickNextPage() {
        return clickPageControl(INDEX_NEXT_PAGE);
    }

    /**
     * Clicks the last page double forward chevron.
     *
     * @return This object.
     */
    public PaginatorComponent clickLastPage() {
        return clickPageControl(INDEX_LAST_PAGE);
    }
}
