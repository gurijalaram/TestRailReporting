package com.apriori.pageobjects.header;

import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsPageHeader extends PageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ReportsPageHeader.class);

    @FindBy(xpath = "//div[@id='reportViewFrame']/div/div/div[@class='title']")
    private WebElement reportTitle;

    @FindBy(id = "dataTimestampMessage")
    private WebElement timestamp;

    @FindBy(id = "dataRefreshButton")
    private WebElement refreshButton;

    @FindBy(id = "back")
    private WebElement backButton;

    @FindBy(id = "export")
    private WebElement exportButton;

    @FindBy(id = "undo")
    private WebElement undoButton;

    @FindBy(id = "redo")
    private WebElement redoButton;

    @FindBy(id = "undoAll")
    private WebElement undoAllButton;

    @FindBy(id = "ICDialog")
    private WebElement inputControlsButton;

    @FindBy(id = "zoom_out")
    private WebElement zoomOutButton;

    @FindBy(id = "zoom_in")
    private WebElement zoomInButton;

    @FindBy(id = "zoom_value_button")
    private WebElement zoomValueDropdown;

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

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public ReportsPageHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
