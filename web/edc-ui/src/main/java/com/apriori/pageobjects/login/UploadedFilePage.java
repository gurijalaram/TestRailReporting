package com.apriori.pageobjects.login;

import com.apriori.EagerPageComponent;

import com.utils.CostStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class UploadedFilePage extends EagerPageComponent<UploadedFilePage> {

    @FindBy(id = "filter-button")
    private WebElement filterButton;

    @FindBy(id = "export-button")
    private WebElement exportBomButton;

    @FindBy(css = ".cost-status .dropdown-toggle")
    private WebElement costStatusDropdown;

    @FindBy(css = ".filter-actions .form-control")
    private WebElement searchElement;

    @FindBy(css = ".line-item-list .no-content-message")
    private WebElement matchComplete;

    @FindBy(xpath = "//div[@class = 'info']/div")
    private WebElement uploadedBomTitle;

    @FindBy(css = "button[class='btn btn-secondary']")
    private WebElement backButton;

    private ElectronicsDataCollectionPage electronicsDataCollectionPage = new ElectronicsDataCollectionPage(getDriver());

    public UploadedFilePage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(filterButton);
    }

    /**
     * Get the BOM title name
     *
     * @return String
     */
    public String getBomTitleName() {
        return getPageUtils().waitForElementToAppear(uploadedBomTitle)
            .getText();
    }

    /**
     * Click on the Matched part
     *
     * @return new page object
     */
    public MatchedPartPage selectMatchedPart(String part) {
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//div[@class='card-title']//div[text()='%s']", part)));
        return new MatchedPartPage(getDriver());
    }

    /**
     * go back to the main page - ElectronicsDataCollectionPage
     *
     * @return new page object
     */
    public ElectronicsDataCollectionPage backToElectronicsDataCollectionPage() {
        getPageUtils().waitForElementAndClick(backButton);
        return new ElectronicsDataCollectionPage(getDriver());
    }


    /**
     * Filter drop down
     *
     * @return new page object
     */
    public UploadedFilePage filterDropdown() {
        getPageUtils().waitForElementAndClick(filterButton);
        setPagination();
        return this;
    }

    /**
     * Select Export Bill of materials
     *
     * @return same page object
     */
    public UploadedFilePage selectExportBom() {
        getPageUtils().waitForElementAndClick(exportBomButton);
        return this;
    }

    /**
     * Search for component
     *
     * @param searchItem the component name
     * @return same page object
     */
    public UploadedFilePage selectSearch(String searchItem) {
        getPageUtils().clearValueOfElement(searchElement);
        searchElement.sendKeys(searchItem);
        return this;
    }

    /**
     * Selects Cost status items
     *
     * @param costStatus - cost status items
     * @return - same page object
     */
    public UploadedFilePage selectCostStatus(CostStatusEnum costStatus) {
        getPageUtils().javaScriptClick(costStatusDropdown);
        By costMatchItem = By.cssSelector(String.format(".dropdown-outline [value='%s']", costStatus.getCostStatus()));
        getPageUtils().waitForElementAndClick(costMatchItem);
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
        return getPageUtils().waitForElementToAppear(matchComplete).getAttribute("textContent");
    }

    /**
     * Get BomId from current url
     *
     * @return String
     */
    public String getBomIdFromCurrentUrl() {
        String url = getDriver().getCurrentUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }
}


