package com.apriori.pageobjects.admin.pages.manage;

import com.apriori.pageobjects.admin.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemDataExport extends AdminHeader {

    private final Logger logger = LoggerFactory.getLogger(SystemDataExport.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    @FindBy(css = "h1")
    private WebElement manageSystemDataExportTitle;

    @FindBy(id = "ToolTables_exportscheduleslist_0")
    private WebElement editSystemDataExportButton;

    @FindBy(xpath = "//input[@value='ONCE']")
    private WebElement editSystemDataExportOnceButton;

    @FindBy(xpath = "//div[@id='DTE_Field_schedule.onceDateTime']/span")
    private WebElement setCurrentDateButton;

    @FindBy(xpath = "//label[@for='DTE_Field_schedule.onceDateTime']")
    private WebElement dateTimeTitle;

    @FindBy(xpath = "//div[@class='DTE_Form_Buttons']/button")
    private WebElement updateButton;

    @FindBy(xpath = "//a[@href='#tab_exporthistories']")
    private WebElement viewHistoryTab;

    @FindBy(xpath = "//a[@id='ToolTables_exporthistorieslist_0']/span")
    private WebElement refreshButton;

    @FindBy(xpath = "//table[@id='exporthistorieslist']/tbody")
    private WebElement exportHistoryTableBody;

    @FindBy(xpath = "//table[@id='exporthistorieslist']/tbody/tr[1]/td[1]")
    private WebElement exportHistoryFirstUserCellInTable;

    @FindBy(xpath = "//table[@id='exporthistorieslist']/tbody/tr[1]/td[4]/span")
    private WebElement exportHistoryFirstStatusCellInTable;

    public SystemDataExport(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets isDisplayed value for header
     *
     * @return boolean - isDisplayed
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(manageSystemDataExportTitle);
        return pageUtils.isElementDisplayed(manageSystemDataExportTitle);
    }

    /**
     * Gets isEnabled value for header
     *
     * @return boolean - isEnabled
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(manageSystemDataExportTitle);
        return pageUtils.isElementEnabled(manageSystemDataExportTitle);
    }

    /**
     * Get header text
     * @return String
     */
    public String getHeaderText() {
        pageUtils.waitForElementToAppear(manageSystemDataExportTitle);
        return manageSystemDataExportTitle.getText();
    }

    /**
     * Click edit System Data Export
     * @return Instance of System Data Export
     */
    public SystemDataExport clickEditSystemDataExport() {
        pageUtils.waitForElementAndClick(editSystemDataExportButton);
        return this;
    }

    /**
     * Click once button
     * @return Instance of System Data Export
     */
    public SystemDataExport clickOnce() {
        pageUtils.waitForElementAndClick(editSystemDataExportOnceButton);
        return this;
    }

    /**
     * Sets current date
     * @return Instance of System Data Export
     */
    public SystemDataExport clickSetDateCurrent() {
        pageUtils.waitForElementAndClick(setCurrentDateButton);
        dateTimeTitle.click();
        return this;
    }

    /**
     * Click update button
     * @return Instance of System Data Export
     */
    public SystemDataExport clickUpdate() {
        pageUtils.waitForElementAndClick(updateButton);
        return this;
    }

    /**
     * Click view history tab
     * @return Instance of System Data Export
     */
    public SystemDataExport clickViewHistory() {
        pageUtils.waitForElementAndClick(viewHistoryTab);
        return this;
    }

    /**
     * Click refresh button
     * @return Instance of System Data Export
     */
    public SystemDataExport clickRefreshButton() {
        pageUtils.waitForElementAndClick(refreshButton);
        return this;
    }

    /**
     * Get count of rows in table
     * @return Integer
     */
    public Integer getTableRowCount() {
        return Integer.parseInt(exportHistoryTableBody.getAttribute("childElementCount"));
    }

    /**
     * Get first user in table
     * @return String
     */
    public String getFirstUserInTable() {
        return exportHistoryFirstUserCellInTable.getText();
    }

    /**
     * Get first user in table
     * @return String
     */
    public String getFirstStatusInTable() {
        return exportHistoryFirstStatusCellInTable.getText();
    }
}
