package com.apriori.cir.ui.pageobjects.header;

import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ReportsPageHeader extends ReportsHeader {

    @FindBy(xpath = "//div[@id='reportViewFrame']/div/div/div[@class='title']")
    private WebElement reportTitle;

    @FindBy(id = "dataTimestampMessage")
    private WebElement timestamp;

    @FindBy(id = "dataRefreshButton")
    private WebElement refreshButton;

    @FindBy(id = "back")
    protected WebElement backButton;

    @FindBy(id = "export")
    private WebElement exportButton;

    @FindBy(id = "undo")
    private WebElement undoButton;

    @FindBy(id = "redo")
    private WebElement redoButton;

    @FindBy(id = "undoAll")
    private WebElement undoAllButton;

    @FindBy(id = "ICDialog")
    protected WebElement inputControlsButton;

    @FindBy(id = "zoom_out")
    protected WebElement zoomOutButton;

    @FindBy(id = "zoom_in")
    protected WebElement zoomInButton;

    @FindBy(id = "zoom_value_button")
    protected WebElement zoomValueDropdown;

    @FindBy(id = "search_report")
    private WebElement searchReportInput;

    @FindBy(id = "search_report_button")
    private WebElement searchReportButton;

    @FindBy(id = "search_options")
    private WebElement searchOptionsDropdown;

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(id = "loading")
    private WebElement loadingPopup;

    @FindBy(xpath = "//h2[contains(text(), 'Domains')]")
    private WebElement domainsItem;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public ReportsPageHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits for home page to load
     * @return instance of ReportsPageHeader
     */
    public ReportsPageHeader waitForHomePageToLoad() {
        pageUtils.waitForElementToAppear(domainsItem);
        return this;
    }

    /**
     * Wait for page to load before continuing
     *
     * @return current page object
     */
    public GenericReportPage waitForInputControlsLoad() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(applyButton);
        return new GenericReportPage(driver);
    }

    /**
     * Click options button to bring up Input Controls
     *
     * @return Input Controls page object
     */
    public GenericReportPage clickInputControlsButton() {
        pageUtils.waitForElementToAppear(inputControlsButton);
        pageUtils.waitForElementToAppear(By.id("schedule"));
        pageUtils.waitForElementToAppear(By.id("embed"));
        pageUtils.waitForElementToAppear(By.id("export"));
        pageUtils.waitForElementToAppear(By.id("back"));
        pageUtils.waitForElementToAppear(By.id("search_report"));
        pageUtils.waitForElementToAppear(By.id("search_report_button"));
        pageUtils.waitForElementToAppear(By.id("search_options"));
        pageUtils.waitForElementToAppear(By.id("dataRefreshButton"));
        inputControlsButton.click();
        return new GenericReportPage(driver);
    }

    /**
     * Waits for no data available to appear
     *
     * @return instance of GenericReportPage
     */
    public GenericReportPage waitForNoDataAvailable() {
        pageUtils.waitForElementToAppear(By.xpath("//span[contains(text(), 'No data available')]"));
        return new GenericReportPage(driver);
    }

    /**
     * Check if Create button is displayed
     *
     * @return Visibility of button
     */
    public boolean isCreateDashboardsButtonDisplayed() {
        pageUtils.waitForElementToAppear(createDashboardsButton);
        return createDashboardsButton.isDisplayed();
    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getTitleText() {
        pageUtils.waitForElementToAppear(pageTitle);
        return pageTitle.getText();
    }
}
