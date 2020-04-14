package com.apriori.pageobjects.reports.header;

import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.PageUtils;

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
    private WebElement optionsButton;

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
        pageUtils.waitForElementToAppear(applyButton);
        return new GenericReportPage(driver);
    }

    /**
     * Click options button to bring up Input Controls
     *
     * @return Input Controls page object
     */
    public GenericReportPage clickInputControlsButton() {
        pageUtils.waitForElementToAppear(optionsButton);
        optionsButton.click();
        return new GenericReportPage(driver);
    }

    /**
     * Returns header to check
     *
     * @return
     */
    public String getHeaderToCheck() {
        return pageUtils.getHeaderToCheck();
    }
}
