package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.UIUtils;

public class NewWorkflowPage {
    private final Logger logger = LoggerFactory.getLogger(NewWorkflowPage.class);

    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83")
    private WebElement newWorkflowModal;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_label-12")
    private WebElement createNewWorkflow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textbox-148 > table > tbody > tr > td > input")
    private WebElement newWorflowNameField;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(7)")
    private WebElement newWorkflowConnectorSelection;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154-bounding-box")
    private WebElement newWorkflowConnectorDropDown;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154")
    private WebElement newWorkflowConnectorDropDownOpen;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-92 > button")
    private WebElement newWorkflowNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-189 > button")
    private  WebElement queryAddRowButton;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(2) > div:nth-child(2)")
    private WebElement ciConnectFieldSelection;
    @FindBy(id = "CIC_CostingInputCell_MU-5_DrowpdownWidget-4")
    private WebElement mappingRuleDD;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(3)")
    private WebElement mappingRuleSelection;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(1)")
    private WebElement valueSelection;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-108 > button")
    private WebElement queryPageNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-43 > button")
    private WebElement saveButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_crontabBuilderWidget-115")
    private WebElement scheduleTabs;
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
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textarea-152 > div > textarea")
    private WebElement newWorkflowDecription;
    @FindBy(id = "root_pagemashupcontainer-1_navigation-83-popup_checkbox-149-input")
    private WebElement newWorkflowEnabledCB;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-183 > button")
    private WebElement newWorkflowCancelButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div.rule-filter-container > select")
    private WebElement queryDropDown;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div.rule-value-container" +
            " > input")
    private WebElement queryValue;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110")
    private WebElement queryPopup;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-104 > button")
    private WebElement queryNext;

    private String ciConnectFieldCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-3";
    private String valueDDCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-20";

    private WebDriver driver;
    private PageUtils pageUtils;
    private UIUtils uiUtils;

    public NewWorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.pageUtils = PageUtils.getInstance(driver);
        this.uiUtils = new UIUtils();
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    public boolean modalExists() {
        return newWorkflowModal.isDisplayed();
    }

    public String getLabel() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(createNewWorkflow));
        return createNewWorkflow.getText();

    }

    public void createNewWorkFlow(String name, int iteration) {
        // Fill Details page
        pageUtils.waitForElementAndClick(newWorflowNameField);
        newWorflowNameField.sendKeys(name);
        pageUtils.waitForElementAndClick(newWorkflowConnectorDropDown);
        pageUtils.waitForElementAndClick(newWorkflowConnectorSelection);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        pageUtils.waitForElementAndClick(newWorkflowNextButton);

        fillQueryDefinitions();
        fillCostingInputs(iteration);

        pageUtils.waitForElementAndClick(saveButton);
    }

    private WebElement getIncrementedElement(String css, int interation) {
        Integer id = interation * 5;
        String elementCss = css.replace("[ID]", id.toString());
        WebElement webElement = driver.findElement(By.cssSelector(elementCss));
        return webElement;
    }

    private void fillQueryDefinitions() {
        Select opt = new Select(queryDropDown);
        opt.selectByIndex(1);
        pageUtils.waitForElementAndClick(queryValue);
        queryValue.sendKeys(Constants.DEFAULT_PART_ID);
        pageUtils.waitForElementAndClick(queryPopup);
        pageUtils.waitForElementAndClick(queryNext);
        pageUtils.waitForElementAndClick(queryAddRowButton);
    }

    private void fillCostingInputs(int iteration) {
        WebElement ciConnectField = getIncrementedElement(ciConnectFieldCss, iteration);
        pageUtils.waitFor(3000);
        pageUtils.waitForElementAndClick(ciConnectField);
        pageUtils.waitForElementAndClick(ciConnectFieldSelection);
        pageUtils.waitFor(3000);
        WebElement valueDD = getIncrementedElement(valueDDCss, iteration);
        pageUtils.waitForElementAndClick(valueDD);
        pageUtils.waitForElementAndClick(valueSelection);
        pageUtils.waitForElementAndClick(queryPageNextButton);
    }
}
