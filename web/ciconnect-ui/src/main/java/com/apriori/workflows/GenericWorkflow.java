package com.apriori.workflows;

import com.apriori.connectors.ConnectorList;
import com.apriori.header.CostingServiceSettings;
import com.apriori.header.PageHeader;
import com.apriori.users.UserList;
import com.apriori.utils.PageUtils;

import cicuserguide.CicUserGuide;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericWorkflow extends LoadableComponent<GenericWorkflow> {

    private static final Logger logger = LoggerFactory.getLogger(GenericWorkflow.class);

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'1')]")
    private WebElement scheduleTab;

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'2')]")
    private WebElement scheduleHistoryTab;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PageHeader pageHeader;

    public GenericWorkflow(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.pageHeader = new PageHeader(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(scheduleTab);
        pageUtils.waitForElementToAppear(workflowLabel);
    }

    public UserList clickUsersMenu() {
        return pageHeader.clickUsersMenu();
    }

    public ConnectorList clickConnectorsMenu() {
        return pageHeader.clickConnectorsMenu();
    }

    /**
     * clicks on schedule tab
     *
     * @return new Schedule page
     */
    public Schedule clickScheduleTab() {
        scheduleTab.click();
        return new Schedule(driver);
    }

    /**
     * clicks on view history tab
     *
     * @return new History page
     */
    public History clickScheduleHistoryTab() {
        scheduleHistoryTab.click();
        return new History(driver);
    }

    /**
     * Get Workflow text
     *
     * @return String
     */
    public String getWorkflowText() {
        return workflowLabel.getText();
    }

    /**
     * Expand user info drop down
     *
     * @return PageHeader page object
     */
    public PageHeader expandUserInfoDropdown() {
        pageHeader.expandUserInfoDropdown();
        return new PageHeader(driver);
    }

    /**
     * Navigate to CIC userguide
     *
     * @return CicUserGuide page object
     */
    public CicUserGuide navigateToCicUserGuide() {
        pageHeader.navigateToCicUserGuide();
        return new CicUserGuide(driver);
    }

    /**
     * Navigate to about aPriori page
     *
     * @return CicUserGuide page object
     */
    public PageHeader navigateToAboutAPriori() {
        pageHeader.navigateToAboutAPriori();
        return new PageHeader(driver);
    }

    /**
     * Open Costing Service Settings modal
     *
     * @return new page object
     */
    public CostingServiceSettings openCostingServiceSettings() {
        pageHeader.openCostingServiceSettings();
        return new CostingServiceSettings(driver);
    }

}