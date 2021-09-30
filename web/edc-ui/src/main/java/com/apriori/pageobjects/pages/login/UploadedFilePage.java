package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import com.utils.CostStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class UploadedFilePage extends LoadableComponent<UploadedFilePage> {

    @FindBy(css = "[data-icon='exclamation-circle']")
    private WebElement fileOne;

    @FindBy(id = "filter-button")
    private WebElement filterButton;

    @FindBy(id = "export-button")
    private WebElement exportBomButton;

    @FindBy(css = ".dropdown-outline [type='button']")
    private WebElement costStatusDropdown;

    @FindBy(css = ".filter-actions .form-control")
    private WebElement searchElement;

    @FindBy(css = ".line-item-list .no-content-message")
    private WebElement matchComplete;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ElectronicsDataCollectionPage electronicsDataCollectionPage;

    public UploadedFilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.electronicsDataCollectionPage = new ElectronicsDataCollectionPage(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(filterButton);
    }

    /**
     * Click on the Matched part
     *
     * @return new page object
     */
    public MatchedPartPage selectMatchedPart() {
        pageUtils.waitForElementAndClick(fileOne);
        return new MatchedPartPage(driver);
    }

    /**
     * Filter drop down
     *
     * @return new page object
     */
    public UploadedFilePage filterDropdown() {
        pageUtils.waitForElementAndClick(filterButton);
        setPagination();
        return this;
    }

    /**
     * Select Export Bill of materials
     *
     * @return same page object
     */
    public UploadedFilePage selectExportBom() {
        pageUtils.waitForElementAndClick(exportBomButton);
        return this;
    }

    /**
     * Clicks on Cost Status dropdown
     *
     * @return same page object
     */
    public UploadedFilePage costStatusDropdown() {
        pageUtils.waitForElementAndClick(costStatusDropdown);
        return this;
    }

    /**
     * Search for component
     *
     * @param searchItem the component name
     * @return same page object
     */
    public UploadedFilePage selectSearch(String searchItem) {
        pageUtils.clear(searchElement);
        searchElement.sendKeys(searchItem);
        return this;
    }

    /**
     * Selects Cost status items
     * @param costStatus
     * @return
     */
    public UploadedFilePage selectCostStatus(CostStatusEnum costStatus) {
        By costMatchItem = By.cssSelector(String.format(".dropdown-outline [value='%s']", costStatus.getCostStatus()));
        pageUtils.waitForElementAndClick(costMatchItem);
        return this;
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public UploadedFilePage setPagination() {
        electronicsDataCollectionPage.setPagination();
        return this;
    }

    /**
     * Get Match complete text
     *
     * @return String
     */
    public String getMatchCompleteText() {
        return pageUtils.waitForElementToAppear(matchComplete).getAttribute("textContent");
    }

    /**
     * Gets the BOM Identity
     *
     * @return String
     */
    public String getBillOfMaterialsId() {
        String currentUrl = driver.getCurrentUrl();
        int billOfMaterialsIdPosition = 4;
        String[] currMatArray = currentUrl.split("/");
        return currMatArray[billOfMaterialsIdPosition];
    }
}
