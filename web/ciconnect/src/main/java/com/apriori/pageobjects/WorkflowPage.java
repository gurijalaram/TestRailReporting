package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Constants;
import utils.TableUtils;
import utils.UIUtils;

import java.util.List;

public class WorkflowPage {
    private final Logger logger = LoggerFactory.getLogger(WorkflowPage.class);

    @FindBy(css = "img[title='Users']")
    private WebElement userTab;
    @FindBy(css = "img[title='Workflows']")
    private WebElement workflowTab;
    @FindBy(css = "img[title='Connectors']")
    private WebElement connectorTab;
    @FindBy(css = "div[title='View History']")
    private WebElement historyTab;
    @FindBy(css = "div[title='Schedule']")
    private WebElement scheduleTab;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody")
    private WebElement workflowList;
    @FindBy(css = "#root_pagemashupcontainer-1_button-35 > button")
    private WebElement newWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-36 > button")
    private WebElement editWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-37 > button")
    private WebElement deleteWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-97 > button")
    private WebElement invokeWorflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-38 > button")
    private WebElement refreshButton;
    @FindBy(css = "#root_pagemashupcontainer-1_tabsv2-10 > div.tab-content > div.tabsv2-viewport > div > div > div:nth-child(2) > div")
    private WebElement viewHistoryTab;

    private String row = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody > " +
            "tr:nth-child({ROWNUMBER})";

    private WebDriver driver;
    private PageUtils pageUtils;
    TableUtils tableUtils;
    UIUtils uiUtils;

    public WorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        pageUtils = PageUtils.getInstance(driver);
        tableUtils = new TableUtils(driver);
        uiUtils = new UIUtils();
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    public void refeshPage(WorkflowPage workflowPage) {
        workflowPage.clickUserTab();
        workflowPage.clickWorkflowTab();
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    public void clickUserTab() {
        pageUtils.waitForElementAndClick(userTab);
    }

    public void clickWorkflowTab() {
        pageUtils.waitForElementAndClick(workflowTab);
    }

    public void refreshWorkflowTable() {
        pageUtils.waitForElementAndClick(refreshButton);
    }

    public Boolean getButtonState(String button) {
        switch (button.toUpperCase()) {
            case "NEW":
                pageUtils.waitForElementToAppear(newWorkflowButton);
                return newWorkflowButton.isEnabled();
            case "EDIT":
                pageUtils.waitForElementToAppear(editWorkflowButton);
                return editWorkflowButton.isEnabled();
            case "DELETE":
                pageUtils.waitForElementToAppear(deleteWorkflowButton);
                return deleteWorkflowButton.isEnabled();
            case "INVOKE":
                pageUtils.waitForElementToAppear(invokeWorflowButton);
                return invokeWorflowButton.isEnabled();
            default:
                return null;
        }
    }

    public void clickInvokeButton() {
        pageUtils.waitForElementAndClick(invokeWorflowButton);
    }

    public void clickDeleteButton() {
        pageUtils.waitForElementAndClick(deleteWorkflowButton);
    }

    public void clickEditButton() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
    }

    public void selectWorkflow(String name) {
        pageUtils.waitForElementAppear(workflowList);
        tableUtils.selectRowByName(workflowList, name);
    }

    public Boolean workflowExists(String name) {
        pageUtils.waitForElementToBeClickable(workflowList);
        return tableUtils.itemExistsInTable(workflowList, name);
    }

    public void newWorkflow() {
        pageUtils.waitForElementAndClick(newWorkflowButton);
    }

    public void editWorkflow() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
    }

    public void clickOnFirstWorkflowRow() {
        tableUtils.selectRowByIndex(workflowList, 1);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    public String getRowName(Integer rowNumber) {
        rowNumber = rowNumber + 1;
        String cssRow = row.replace("{ROWNUMBER}", rowNumber.toString());
        WebElement workflowRow = driver.findElement(By.cssSelector(cssRow));
        List<WebElement> columns = workflowRow.findElements(By.tagName("td"));
        return columns.get(0).getText();
    }
}
