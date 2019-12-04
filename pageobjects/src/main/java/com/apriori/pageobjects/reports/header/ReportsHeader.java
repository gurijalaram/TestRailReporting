package com.apriori.pageobjects.reports.header;

import com.apriori.pageobjects.header.ExploreHeader;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReport;
import com.apriori.pageobjects.reports.pages.view.reports.InputControls;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsHeader extends PageHeader {
    private static Logger logger = LoggerFactory.getLogger(ReportsHeader.class);

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

    private WebDriver driver;
    private PageUtils pageUtils;

    public ReportsHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for page to load before continuing
     * @return current page object
     */
    public InputControls waitForPageLoad() {
        pageUtils.waitForElementToAppear(applyButton);
        return new InputControls(driver);
    }

    /**
     * Click options button to bring up Input Controls
     * @return Input Controls page object
     */
    public InputControls clickOptionsButton() {
        pageUtils.waitForElementToAppear(optionsButton);
        optionsButton.click();
        return new InputControls(driver);
    }
}
