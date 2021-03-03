package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditWorkflowPage {
    private final Logger logger = LoggerFactory.getLogger(EditWorkflowPage.class);

    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup")
    private WebElement editWorkflowModal;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_label-12")
    private WebElement editWorkflowLabel;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_textbox-148 > table > tbody > tr > td > input")
    private WebElement editWorkflowName;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_textarea-152 > div > textarea")
    private WebElement editWorkFlowDescription;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_DrowpdownWidget-154 > div > div")
    private WebElement editWorflowConnector;
    @FindBy(id = "root_pagemashupcontainer-1_navigation-84-popup_checkbox-149-input")
    private WebElement editWorkflowEnabledCB;
    @FindBy(id = "msMinutesTab")
    private WebElement minutesTab;
    @FindBy(id = "MinutesInput")
    private WebElement minutesInput;
    @FindBy(id = "msHourlyTab")
    private WebElement hourlyTab;
    @FindBy(id = "HoursInput")
    private WebElement hoursInput;
    @FindBy(id = "msDailyTab")
    private WebElement dailyTab;
    @FindBy(id = "DaysInput")
    private WebElement daysInput;
    @FindBy(id = "msWeeklyTab")
    private WebElement weeklyTab;
    @FindBy(id = "WeeksInput")
    private WebElement weeksInput;
    @FindBy(id = "msMonthlyTab")
    private WebElement monthlyTab;
    @FindBy(id = "MonthsInput")
    private WebElement monthsInput;
    @FindBy(id = "msYearlyTab")
    private WebElement yearlyTab;
    @FindBy(id = "YearsInput")
    private WebElement yearsInput;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_QueryBuilder-110_rule_0 > div.rule-filter-container > select")
    private WebElement queryDropDown;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-92 > button")
    private WebElement editWorkflowNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-108 > button")
    private WebElement queryPageNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-108 > button")
    private WebElement costingNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-44 > button")
    private WebElement saveButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-183 > button")
    private WebElement editWorkflowCancelButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-84-popup_button-104 > button")
    private WebElement queryNext;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EditWorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.pageUtils = PageUtils.getInstance(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    public void selectQuery() {
        Select opt = new Select(queryDropDown);
        opt.selectByIndex(1);
    }

    public boolean modalExists() {
        pageUtils.waitForElementToAppear(editWorkflowNextButton);
        return editWorkflowModal.isDisplayed();
    }

    public void cancelEdit() {
        pageUtils.waitForElementAndClick(editWorkflowCancelButton);
    }

    public void editWorkflow(String field, String newValue) {
        switch (field.toUpperCase()) {
            case "NAME":
                pageUtils.waitForElementAndClick(editWorkflowName);
                editWorkflowName.clear();
                editWorkflowName.sendKeys(newValue);
                editWorkFlowDescription.click();
                break;
            case "DESCRIPTION":
                pageUtils.waitForElementAndClick(editWorkFlowDescription);
                editWorkFlowDescription.sendKeys(newValue);
                break;
            default:
                logger.debug("Field " + field + ", does not exist");
                return;
        }

        pageUtils.waitForElementAndClick(editWorkflowNextButton);
        pageUtils.waitForElementAndClick(queryNext);
        pageUtils.waitForElementAndClick(queryPageNextButton);
        pageUtils.waitForElementAndClick(saveButton);
    }

    public String getLabel() {
        pageUtils.waitForElementAppear(editWorkflowLabel);
        return editWorkflowLabel.getText();
    }
}
